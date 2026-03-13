package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public static ErrorResponse of(int status, String error, String message, String path) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status);
        response.setError(error);
        response.setMessage(message);
        response.setPath(path);
        return response;
    }
}