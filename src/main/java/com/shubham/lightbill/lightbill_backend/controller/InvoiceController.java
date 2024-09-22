package com.shubham.lightbill.lightbill_backend.controller;

import com.lowagie.text.DocumentException;
import com.shubham.lightbill.lightbill_backend.model.Consumption;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @GetMapping("/hello")
    public Integer sayHello(){
        return 10;
    }

    @GetMapping("/")
    public void getInvoice(HttpServletResponse response) throws IOException {
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

        // Labels and data for the graph
        List<String> pastMonths = Arrays.asList("July", "August", "September", "October", "November");
        List<Integer> pastReadings = Arrays.asList(150, 160, 170, 180, 190);

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
        context.setVariable("labels", pastMonths);
        context.setVariable("data", pastReadings);
        context.setVariable("pastMonths", pastMonths);
        context.setVariable("pastReadings", pastReadings);
        context.setVariable("currentMonthConsumption", new Consumption("December", 190, 95.00));

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"invoice.pdf\"");

        // Process the template and write the output to the response
        String htmlContent = templateEngine.process("invoice-template.html", context);

        try (OutputStream outputStream = response.getOutputStream()) {
            // Parse the HTML to a Jsoup Document
            Document document = Jsoup.parse(htmlContent, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

            // Create PDF using ITextRenderer
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(document.outerHtml());
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.flush();
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }
}
