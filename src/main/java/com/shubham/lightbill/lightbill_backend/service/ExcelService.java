package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.dto.BillDto;
import com.shubham.lightbill.lightbill_backend.dto.SignUpDto;
import com.shubham.lightbill.lightbill_backend.model.Bill;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import com.shubham.lightbill.lightbill_backend.repository.WalletRepository;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private BillService billService;

    public SignUpDto convertToSignUpDto(Row row){
        String name, email, phoneNo, address, role;
        name = row.getCell(0).getStringCellValue();
        email = row.getCell(1).getStringCellValue();
        address = row.getCell(3).getStringCellValue();
        role = row.getCell(4).getStringCellValue();

        if(row.getCell(2).getCellType() == CellType.STRING) phoneNo = row.getCell(2).getStringCellValue();
        else phoneNo = (String) String.valueOf(row.getCell(2).getNumericCellValue());

        return SignUpDto.builder()
                .address(address)
                .name(name)
                .email(email)
                .role(Role.valueOf(role))
                .build();
    }

    public BillDto convertToBillDto(Row row){
        String userId = row.getCell(0).getStringCellValue();
        String monthAndYear = row.getCell(1).getStringCellValue();
        Integer unitConsumption = (int) row.getCell(2).getNumericCellValue();
        Date dueDate = row.getCell(3).getDateCellValue();
        Integer discount = (int) row.getCell(4).getNumericCellValue();
        Integer amount = (int) row.getCell(4).getNumericCellValue();

        return BillDto.builder()
                .userId(userId)
                .monthAndYear(monthAndYear)
                .unitConsumption(unitConsumption)
                .dueDate(dueDate)
                .discount(discount)
                .amount(amount)
                .build();
    }

    public List<Object> saveExcelToDatabase(MultipartFile file, String className){
        List<Object> finalResult = new ArrayList<>();
        try(
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
        ){
            Sheet sheet = workbook.getSheetAt(0);

            for (Row currRow : sheet) {
                if (currRow.getRowNum() == 0) {
                    continue;
                }

                List<Object> res = new ArrayList<>();
                if(className.equals(User.class.getName())){
                    SignUpDto signUp = convertToSignUpDto(currRow);
                    res = authService.signUpUser(signUp);
                }
                else if(className.equals(Bill.class.getName())){
                    BillDto billDto = convertToBillDto(currRow);
                    Bill bill = billService.addBill(billDto);
                    finalResult.add(bill);
                }

                finalResult.addAll(res);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return finalResult;
    }

    public List<Object> addBulkBill(MultipartFile file){
        return saveExcelToDatabase(file, Bill.class.getName());
    }
}
