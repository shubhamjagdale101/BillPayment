package com.shubham.lightbill.lightbill_backend.controller;

import com.lowagie.text.DocumentException;
import com.shubham.lightbill.lightbill_backend.annotation.WithRateLimitProtection;
import com.shubham.lightbill.lightbill_backend.model.Consumption;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.PdfService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private PdfService pdfService;

    @GetMapping("/hello")
    @WithRateLimitProtection(rateLimit = 20, rateDuration = 60000)
    public ApiResponse<Integer> sayHello(HttpServletRequest request){
        return ApiResponse.success(20, "success", HttpStatus.OK.value());
    }

    @GetMapping("/upload")
    public ApiResponse<String> uploadFileToCloudinary() throws IOException {
        String res =  pdfService.generateAndUploadInvoice();
        return ApiResponse.success(null, res, HttpStatus.ACCEPTED.value());
    }

    @PostMapping("/invoiceEmail")
    public ApiResponse<String> sendInvoiceEmail(@RequestBody Map<String, String> payload) throws Exception {
        String email = payload.get("email");
        if(email == null) throw new Exception("Email not found in request data");

        pdfService.sendInvoiceOnMail(email);
        return ApiResponse.success(null, "Invoice mail will be send", HttpStatus.ACCEPTED.value());
    }
}
