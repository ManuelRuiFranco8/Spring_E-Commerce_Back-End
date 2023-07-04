package com.psw.project1.controllers;

import com.psw.project1.entities.ProductInCart;
import com.psw.project1.services.*;
import com.psw.project1.utils.exceptions.*;
import com.psw.project1.utils.messages.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductInCartController {

    @Autowired
    private ProductInCartService cartServ;

    @PreAuthorize("hasRole('USER')")
    @PostMapping({"/addToCart/{productId}"})
    public ResponseEntity addToCart(@PathVariable(name="productId") Long productId) {
        try{
            ProductInCart pic=cartServ.addToCart(productId);
            return new ResponseEntity(pic, HttpStatus.OK); //the request returns the newly added product in JSON format
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch
    }//addToCart

    @PreAuthorize("hasRole('USER')")
    @GetMapping({"/getCartDetails"})
    public ResponseEntity getCartDetails() {
        try{
            List<ProductInCart> cartDetails=cartServ.getCartDetails();
            return new ResponseEntity(cartDetails, HttpStatus.OK); //the request returns the newly added product in JSON format
        } catch(Exception e) {
            System.out.println("Procedural Error");
            return new ResponseEntity("Procedural Error", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//addToCart
}//ProductInCartController
