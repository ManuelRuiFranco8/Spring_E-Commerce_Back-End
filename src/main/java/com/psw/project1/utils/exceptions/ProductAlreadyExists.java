package com.psw.project1.utils.exceptions;

public class ProductAlreadyExists extends AppException {
    public ProductAlreadyExists() {
        super();
        msg="This product already exists in the database.";
    }//constructor
}//ProductAlreadyExists
