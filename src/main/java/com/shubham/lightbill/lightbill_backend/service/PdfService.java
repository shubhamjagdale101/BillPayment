package com.shubham.lightbill.lightbill_backend.service;

import com.cloudinary.Cloudinary;
import com.lowagie.text.DocumentException;
import com.shubham.lightbill.lightbill_backend.model.Consumption;
import jakarta.mail.MessagingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PdfService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ThreadPoolService threadPoolService;
    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    public String prepareInvoiceHtmlFile(){
        String customerName = "John Doe";
        String customerAddress = "123 Main St, Springfield";
        String meterNumber = "123456789";
        String dueDate = "2024-12-15";
        double amountBeforeDueDate = 100.00;
        double amountAfterDueDate = 120.00;
        double totalAmountDue = 100.00;

        // Sample consumption data
        List<Consumption> consumptionSummary = Arrays.asList(
                new Consumption("July", 150, 75.00),
                new Consumption("August", 160, 80.00),
                new Consumption("September", 170, 85.00),
                new Consumption("October", 180, 90.00),
                new Consumption("November", 190, 95.00)
        );

        // Create Thymeleaf Context
        Context context = new Context();
        context.setVariable("customerName", customerName);
        context.setVariable("customerAddress", customerAddress);
        context.setVariable("meterNumber", meterNumber);
        context.setVariable("dueDate", dueDate);
        context.setVariable("amountBeforeDueDate", amountBeforeDueDate);
        context.setVariable("amountAfterDueDate", amountAfterDueDate);
        context.setVariable("totalAmountDue", totalAmountDue);
        context.setVariable("consumptionSummary", consumptionSummary);
        context.setVariable("currentMonthConsumption", new Consumption("December", 190, 95.00));

        return templateEngine.process("invoice-template.html", context);
    }

    public ByteArrayOutputStream generateInvoicePdf() throws IOException {
        String htmlContent = prepareInvoiceHtmlFile();

        // Parse the HTML to a Jsoup Document
        Document document = Jsoup.parse(htmlContent, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        // Create PDF using ITextRenderer
        ITextRenderer renderer = new ITextRenderer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{
            // Set the document
            renderer.setDocumentFromString(document.outerHtml());
            renderer.layout(); // Layout the document

            // Create the PDF
            renderer.createPDF(outputStream);
            outputStream.flush(); // Ensure all data is written

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream;
    }

    public String generateAndUploadInvoice() throws IOException {
        // Step 1: Generate the PDF and write it to a temporary file
        ByteArrayOutputStream pdfOutputStream = generateInvoicePdf();
        File tempPdfFile = null;
        UUID uid = UUID.randomUUID();

        try {
            // Create a temporary file to store the PDF
            tempPdfFile = File.createTempFile("invoice", ".pdf");

            // Write the PDF byte array to the temporary file
            try (FileOutputStream fos = new FileOutputStream(tempPdfFile)) {
                fos.write(pdfOutputStream.toByteArray());
            }

            // Step 2: Upload the file to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(tempPdfFile, Map.of(
                    "resource_type", "raw",  // Specify 'raw' for non-image files like PDF
                    "public_id", "invoices/" + uid + "_invoice", // Custom public ID
                    "overwrite", true // Overwrite if it already exists
            ));

            // Return the URL of the uploaded PDF from Cloudinary
            String prefix = "https://res-console.cloudinary.com/dwymu1d8q/media_explorer_thumbnails/";
            String suffix = "/download";
            String asset_id = (String) uploadResult.get("asset_id");

            return prefix + asset_id + suffix;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload PDF to Cloudinary", e);
        } finally {
            // Step 3: Clean up temporary file after uploading
            if (tempPdfFile != null && tempPdfFile.exists()) {
                tempPdfFile.delete();
            }
        }
    }

    public void sendInvoiceOnMail(String email) throws IOException, MessagingException {
        Runnable task = () -> {
            File tempPdfFile = null;
            try {
                // Create the temporary file before using it
                tempPdfFile = File.createTempFile("invoice", ".pdf");

                // Generate the PDF and write to the temporary file
                try (FileOutputStream fos = new FileOutputStream(tempPdfFile);
                     ByteArrayOutputStream pdfOutputStream = generateInvoicePdf()) {
                    fos.write(pdfOutputStream.toByteArray());
                }

                // Prepare email details
                String subject = "Reminder: Electricity Bill for [Month, Year]";
                String customerName = "Shubham";
                String month = "September";
                String year = "2024";
                String attachmentFileName = "Invoice";

                String body = "Dear " + customerName + ",\n\n"
                        + "I hope this message finds you well.\n\n"
                        + "Please find attached your electricity bill for the month of " + month + ", " + year + ". We kindly request you to review the bill, which provides a detailed breakdown of your electricity usage and charges during the specified period.\n\n"
                        + "If you have any questions or concerns regarding the bill, feel free to contact our support team, and we’ll be happy to assist you.\n\n"
                        + "We appreciate your prompt attention to this matter. Kindly make the payment by the due date to avoid any service interruptions.\n\n"
                        + "Thank you for choosing our service.\n\n"
                        + "Best regards,\n"
                        + "[Your Name]\n"
                        + "[Your Company Name]\n"
                        + "[Contact Information]";

                // Send the email with the attachment
                emailService.sendMailWithInvoiceAttachment(subject, email, tempPdfFile, body, attachmentFileName);
            } catch (IOException | MessagingException e) {
                logger.error("Error sending invoice on mail: " + e.getMessage(), e);
            } finally {
                // delete temp file after work
                if (tempPdfFile != null && tempPdfFile.exists()) {
                    boolean deleted = tempPdfFile.delete();
                    if (!deleted) {
                        logger.warn("Failed to delete temporary file: " + tempPdfFile.getAbsolutePath());
                    }
                }
            }
        };

        threadPoolService.submitTask(task);
    }
}
