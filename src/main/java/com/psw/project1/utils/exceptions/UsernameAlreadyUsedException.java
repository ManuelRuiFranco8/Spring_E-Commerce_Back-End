package com.psw.project1.utils.exceptions;

public class UsernameAlreadyUsedException extends AppException {
    public UsernameAlreadyUsedException() {
        super();
        msg="An user is already registered with this username. Please use another one.";
    }//constructor
}//UsernameAlreadyUsedException
