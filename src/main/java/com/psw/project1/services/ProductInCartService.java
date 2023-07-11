package com.psw.project1.services;

import com.psw.project1.configurations.JwtRequestFilter;
import com.psw.project1.entities.*;
import com.psw.project1.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import java.io.IOException;
import java.util.*;

@Service
public class ProductInCartService {

    @Autowired
    private ProductInCartRepository cartRep;

    @Autowired
    private ProductRepository productRep;

    @Autowired
    private UserRepository userRep;

    public ProductInCart addToCart(Long productId) throws IOException {
        boolean alreadyPresent=false;
        Product product=productRep.findById(productId).get();
        String username=JwtRequestFilter.currentUser();
        User user=null;
        if(username!=null) {
            user=userRep.findByUsername(username).get(0);
        }//if
        List<ProductInCart> cart=cartRep.findByUser(user);
        for(ProductInCart pic: cart) {
            if(pic.getProduct().getId()==productId) { //if the product is already present in current user's cart
                alreadyPresent=true;
                break;
            }//if
        }//for
        if(alreadyPresent) {
            return null; //do not add the product to the cart if already present
        }//if
        if(product!=null && user!=null) {
            ProductInCart pic=new ProductInCart(product, user);
            return cartRep.save(pic);
        } else {
            throw new IOException("Impossible to add the product to the user's cart");
        }//if-else
    }//addToCart

    public List<ProductInCart> getCartDetails() throws IOException {
        String username=JwtRequestFilter.currentUser();
        User user=userRep.findByUsername(username).get(0);
        if(user!=null) {
            return cartRep.findByUser(user);
        } else {
            throw new IOException();
        }//if-else
    }//getCartDetails

    public void deleteCartItem(Long cartId) {
        cartRep.deleteById(cartId);
    }//deleteCartItem
}//ProductInCartService
