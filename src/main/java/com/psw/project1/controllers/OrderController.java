package com.psw.project1.controllers;

import com.psw.project1.services.*;
import com.psw.project1.utils.exceptions.*;
import com.psw.project1.utils.messages.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderServ;

    @PreAuthorize("hasRole('USER')")
    @PostMapping({"/placeOrder/{singleProduct}"})
    public ResponseEntity placeOrder(@PathVariable(name="singleProduct") boolean singleProduct,
                                     @RequestBody OrderRequest request) {
        try{
            orderServ.placeOrder(request, singleProduct);
            return new ResponseEntity(HttpStatus.OK); //the request returns the newly added product in JSON format
        } catch(AppException ae) {
            System.out.println(ae.getMsg());
            return new ResponseEntity(ae.getMsg(), HttpStatus.CONFLICT);
        }catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Procedural error. Impossible to place order", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//placeOrder
}//OrderContrller