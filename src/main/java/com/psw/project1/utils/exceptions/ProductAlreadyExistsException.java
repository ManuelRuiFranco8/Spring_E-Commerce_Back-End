package com.psw.project1.utils.exceptions;

public class ProductAlreadyExistsException extends AppException {
    public ProductAlreadyExistsException() {
        super();
        msg="This product already exists in the database.";
    }//constructor
}//ProductAlreadyExists
