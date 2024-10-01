package com.shubham.lightbill.lightbill_backend.controller;

import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import com.shubham.lightbill.lightbill_backend.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public ApiResponse<Object> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Please upload an Excel file");
        }

        List<Object> res = excelService.saveExcelToDatabase(file, User.class.getName(), Role.CUSTOMER);
        return ApiResponse.success(res, "File uploaded and data saved to the database successfully.", HttpStatus.ACCEPTED.value());
    }

    @PostMapping("/addBulkBillByExcel")
    public ApiResponse<List<Object>> addBulBill(MultipartFile file){
        List<Object> res = excelService.addBulkBill(file, Role.CUSTOMER);
        return ApiResponse.success(res, "Bulk Bill get Added", HttpStatus.OK.value());
    }
}
