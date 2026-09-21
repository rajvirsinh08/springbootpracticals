package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private int status;
    private String path;
    private Map<String, String> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime timestamp,String message,int status,String path) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
        this.path = path;
    }

    public ErrorResponse(LocalDateTime timestamp,String message,int status,String path,Map<String, String> errors) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
        this.path = path;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}