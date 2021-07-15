package com.cinema;

public class Error {
    private String error;

    public Error(String error) {
        this.error = error;
    }
    Error(){};

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
