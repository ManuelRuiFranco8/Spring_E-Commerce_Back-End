package com.psw.project1.controllers;

import com.psw.project1.entities.ProductInCart;
import com.psw.project1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductInCartController {

    @Autowired
    private ProductInCartService cartServ;

    @PreAuthorize("hasRole('USER')") //only the user role may be able to access this endpoint
    @PostMapping({"/addToCart/{productId}"}) //back-end endpoint to add a product to the current user's cart
    public ResponseEntity addToCart(@PathVariable(name="productId") Long productId) {
        try{
            ProductInCart pic=cartServ.addToCart(productId);
            return new ResponseEntity(pic, HttpStatus.OK); //product successfully added to current user's cart
        } catch(IOException e) { //the product cannot be added to the cart
            System.out.println(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch
    }//addToCart

    @PreAuthorize("hasRole('USER')") //only the user role may be able to access this endpoint
    @GetMapping({"/getCartDetails"}) //back-end endpoint to fetch all current user's cart items
    public ResponseEntity getCartDetails() {
        try{
            List<ProductInCart> cartDetails=cartServ.getCartDetails();
            return new ResponseEntity(cartDetails, HttpStatus.OK); //cart items successfully fetched
        } catch(IOException e) { //cart items cannot be fetched
            System.out.println("Procedural Error");
            return new ResponseEntity("Procedural Error", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//addToCart

    @PreAuthorize("hasRole('USER')") //only the user role may be able to access this endpoint
    @DeleteMapping({"/removeProduct/{cartId}"}) //back-end endpoint to delete a item from current user's cart
    public void deleteCartItem(@PathVariable(name="cartId") Long cartId) {
        cartServ.deleteCartItem(cartId);
    }//deleteCartItem
}//ProductInCartController