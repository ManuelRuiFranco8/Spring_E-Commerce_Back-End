package com.psw.project1.utils.exceptions;

public class ProductOutOfStockException extends AppException {
    public ProductOutOfStockException() {
        super();
        msg="Product's stock unsufficient to satisfy the order.";
    }//constructor
}//ProductOutOfStockException
