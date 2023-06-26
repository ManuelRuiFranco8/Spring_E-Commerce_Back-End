package com.psw.project1.utils.exceptions;

public class UserNotFoundException extends AppException {

    public UserNotFoundException() {
        super();
        msg="No user corrisponding to search criteria has been found";
    }//constructor
}//UserNotFoundException
