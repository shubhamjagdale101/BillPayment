package com.shubham.lightbill.lightbill_backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private Boolean error;
    private String message;
    private T data;
    private int status;

    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return new ApiResponse<>(false, message, data, status);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(true, message, null, status);
    }
}
