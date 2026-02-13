package com.projeto.estudo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private String message;
    private T data;
    private boolean success;

    public ApiResponse(String message, T data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data, true);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null, false);
    }

}
