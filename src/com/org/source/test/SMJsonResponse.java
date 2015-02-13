package com.org.source.test;

public class SMJsonResponse <T> {
    
    public static class Result {
        private Short status;
        private String message;
        
        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
        
        public void setStatus(Short status) {
            this.status = status;
        }

        public Short getStatus() {
            return status;
        }
    }
    
    private T data;
    private Result result;
    
    public void setData(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
    public Result getResult() {
        return result;
    }
}
