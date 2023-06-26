package com.psw.project1.utils.exceptions;

public class TelephoneNumberAlreadyUsedException extends AppException {
    public TelephoneNumberAlreadyUsedException() {
        super();
        msg="An user is already registered with this telephone number. Please use another one.";
    }//constructor
}//TelephoneNumberAlreadyUsedException
