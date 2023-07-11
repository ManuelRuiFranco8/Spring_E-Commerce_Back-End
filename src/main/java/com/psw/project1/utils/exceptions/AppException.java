package com.psw.project1.utils.exceptions;

public class AppException extends Exception { //custom's class for exceptions specific to the e-commerce app
    protected String msg; //the message will be returned to the endpoint in case of error

    public AppException() {}//constructor

    public String getMsg() {
        return msg;
    }//getMessage
}//AppException
