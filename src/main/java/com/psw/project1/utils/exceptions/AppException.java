package com.psw.project1.utils.exceptions;

public class AppException extends Exception {
    protected String msg;

    public AppException() {}//constructor

    public String getMsg() {
        return msg;
    }//getMessage
}//AppException
