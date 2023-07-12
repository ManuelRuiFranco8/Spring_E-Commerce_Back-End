package com.psw.project1.controllers;

import com.psw.project1.entities.*;
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
public class OrderController {

    @Autowired
    private OrderService orderServ;

    @PreAuthorize("hasRole('USER')") //only the user role may be able to access this endpoint
    @PostMapping({"/placeOrder/{singleProduct}"}) //back-end endpoint to place an order
    public ResponseEntity placeOrder(@PathVariable(name="singleProduct") boolean singleProduct,
                                     @RequestBody OrderRequest request) {
        try{
            orderServ.placeOrder(request, singleProduct);
            return new ResponseEntity(HttpStatus.OK); //order successfully placed
        } catch(AppException ae) { //order cannot be placed for application-specific exception
            System.out.println(ae.getMsg());
            return new ResponseEntity(ae.getMsg(), HttpStatus.CONFLICT);
        }catch(Exception e) { //order cannot be placed for generic exception
            System.out.println(e.getMessage());
            return new ResponseEntity("Procedural error. Impossible to place order", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//placeOrder

    @PreAuthorize("hasRole('USER')") //only the user role may be able to access this endpoint
    @GetMapping({"/getUserOrders/{status}"}) //back-end endpoint to get all orders associated to current user
    public ResponseEntity getOrdersList(@PathVariable(name="status") String status) {
        try{
            List<Order> list=orderServ.getOrdersList(status);
            return new ResponseEntity(list, HttpStatus.OK); //orders successfully fetched
        }catch(Exception e) { //orders cannot be fetched
            System.out.println(e.getMessage());
            return new ResponseEntity("Procedural error. Impossible fetch order list", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//placeOrder
}//OrderContrller