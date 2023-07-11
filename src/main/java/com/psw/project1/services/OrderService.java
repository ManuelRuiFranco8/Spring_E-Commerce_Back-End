package com.psw.project1.services;

import com.psw.project1.configurations.JwtRequestFilter;
import com.psw.project1.entities.*;
import com.psw.project1.repositories.*;
import com.psw.project1.utils.enums.Order_Status;
import com.psw.project1.utils.enums.Shipment_Type;
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
        int addShipTax=1;
        for(ProductQuantity pq: productQuantities) {
            Product p=productRep.findById(pq.getProductId()).get();
            if(p.getQuantity()-pq.getQuantity()<0) {
                throw new ProductOutOfStockException();
            } else {
                p.setQuantity(p.getQuantity()-pq.getQuantity());
            }//if-else
            double amount=(double) p.getPrice()*pq.getQuantity();
            if(addShipTax==productQuantities.size()) { //add the shipment fee to the last product
                if(request.getShipment()==Shipment_Type.STANDARD) {
                    amount+=2.99;
                } else {
                    amount+=10.99;
                }//if-else
            }//if
            Order o=new Order(Order_Status.PLACED, amount,
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

    public List<Order> getOrdersList(String status) {
        String user=JwtRequestFilter.currentUser();
        User u=userRep.findByUsername(user).get(0);

        List<Order> list=null;
        if(status.equals("ALL")) {
            list=orderRep.findByBuyingUser(u);
        } else if(status.equals("PLACED")) {
            list=orderRep.findByStatus(u,Order_Status.PLACED);
        } else {
            list=orderRep.findByStatus(u,Order_Status.RECEIVED);
        }//if-else

        for(Order o: list) {
            Date current=new Date();
            if(o.getShipmentDate()!=null && o.getShipmentDate().compareTo(current)<0) {
                o.setStatus(Order_Status.RECEIVED);
                orderRep.save(o);
            }//if
        }//for
        return list;
    }//getOrdersList
}//OrderService
