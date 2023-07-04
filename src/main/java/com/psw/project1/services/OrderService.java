package com.psw.project1.services;

import com.psw.project1.configurations.JwtRequestFilter;
import com.psw.project1.entities.*;
import com.psw.project1.repositories.*;
import com.psw.project1.utils.enums.Order_Status;
import com.psw.project1.utils.exceptions.*;
import com.psw.project1.utils.messages.OrderRequest;
import com.psw.project1.utils.messages.ProductQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRep;

    @Autowired
    private ProductRepository productRep;

    @Autowired
    private UserRepository userRep;

    @Autowired
    private ProductInCartRepository cartRep;

    public void placeOrder(OrderRequest request, boolean singleProduct) throws AppException {
        List<ProductQuantity> productQuantities=request.getProductsQuantityList();
        String currentUser=JwtRequestFilter.currentUser();
        User u=userRep.findByUsername(currentUser).get(0);
        for(ProductQuantity pq: productQuantities) {
            Product p=productRep.findById(pq.getProductId()).get();
            if(p.getQuantity()-pq.getQuantity()<0) {
                throw new ProductOutOfStockException();
            } else {
                p.setQuantity(p.getQuantity()-pq.getQuantity());
            }//if-else
            Order o=new Order(Order_Status.PLACED, (double) p.getPrice()*pq.getQuantity(),
                              p, u, request.getContact(), request.getShipment());
            orderRep.save(o);
        }//for
        if(!singleProduct) {//empty the current user's cart
            List<ProductInCart> cart=cartRep.findByUser(u);
            for(ProductInCart pic: cart) {
                cartRep.delete(pic);
            }//for
            //cart.stream().forEach(x->cartRep.delete(x));
        }//if
    }//placeOrder
}//OrderService
