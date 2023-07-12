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
        User u=userRep.findByUsername(currentUser).get(0); //gets the user currently placing the order
        int addShipTax=1;
        for(ProductQuantity pq: productQuantities) { //for every product in the order
            Product p=productRep.findById(pq.getProductId()).get(); //fetches the product
            if(p.getQuantity()-pq.getQuantity()<0) { //if available product's stock is insufficient for the order
                throw new ProductOutOfStockException();
            } else { //if available product's stock is sufficient for the order
                p.setQuantity(p.getQuantity()-pq.getQuantity()); //updates available product's stock
            }//if-else
            double amount=(double) p.getPrice()*pq.getQuantity(); //order partial amount (relative to current product)
            if(addShipTax==productQuantities.size()) { //add the shipment fee to the last product
                if(request.getShipment().equals(Shipment_Type.STANDARD)) {
                    amount+=2.99;
                } else if (request.getShipment().equals(Shipment_Type.PREMIUM)){
                    amount+=10.99;
                }//if-else
            } else {
                addShipTax+=1;
            }//if-else
            Order o=new Order(Order_Status.PLACED, amount, p, u, request.getContact(), request.getShipment());
            orderRep.save(o); //save the order in the database (the order is issued)
        }//for
        if(!singleProduct) {//empty the current user's cart (if we are buying from the cart)
            List<ProductInCart> cart=cartRep.findByUser(u); //takes all product in cart item associated to current user
            for(ProductInCart pic: cart) {
                cartRep.delete(pic); //deletes product in cart item
            }//for
        }//if
    }//placeOrder

    public List<Order> getOrdersList(String status) {
        String user=JwtRequestFilter.currentUser();
        User u=userRep.findByUsername(user).get(0); //fetches current user
        List<Order> list;
        if(status.equals("ALL")) {
            list=orderRep.findByBuyingUser(u); //fetches all past orders of the current users
        } else if(status.equals("PLACED")) {
            list=orderRep.findByStatus(u,Order_Status.PLACED); //fetches orders of the current user in issued state
        } else {
            list=orderRep.findByStatus(u,Order_Status.RECEIVED); //fetches orders of the current user in received state
        }//if-else
        for(Order o: list) {
            Date current=new Date();
            if(o.getShipmentDate()!=null && o.getShipmentDate().compareTo(current)<0) { //if order has been received
                o.setStatus(Order_Status.RECEIVED); //updates order status
                if(status=="PLACED") { //if we are fetching placed orders
                    list.remove(o); //if the order has become received, we do not show it
                }//if
                orderRep.save(o); //updates order with new status
            }//if
        }//for
        return list;
    }//getOrdersList (fetches past orders of the current user)
}//OrderService
