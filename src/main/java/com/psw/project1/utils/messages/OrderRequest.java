package com.psw.project1.utils.messages;

import com.psw.project1.utils.enums.Shipment_Type;
import com.psw.project1.utils.enums.User_Contact;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderRequest { //user's request issued to place an order
    private List<ProductQuantity> productsQuantityList; //a list of <product (id), quantity> couples
    private User_Contact contact; //user's contact preferences
    private Shipment_Type shipment; //user's shipment preferences
}//OrderRequest
