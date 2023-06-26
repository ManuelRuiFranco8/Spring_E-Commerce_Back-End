package com.psw.project1.utils.exceptions;

public class MailAlreadyUsedException extends AppException {
    public MailAlreadyUsedException() {
        super();
        msg="An user is already registered with this email. Please use another one.";
    }//constructor
}//MailAlreadyUsedException
