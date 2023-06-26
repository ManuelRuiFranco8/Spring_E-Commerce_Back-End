package com.psw.project1.utils.exceptions;

public class ProductNotFoundException extends AppException {
    public ProductNotFoundException() {
        super();
        msg="No corresponding product exists in the database ";
    }//constructor
}//ProductAlreadyExists}
