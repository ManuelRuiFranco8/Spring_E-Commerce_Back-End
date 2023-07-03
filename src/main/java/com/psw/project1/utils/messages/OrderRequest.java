package com.psw.project1.utils.messages;

import com.psw.project1.utils.enums.Shipment_Type;
import com.psw.project1.utils.enums.User_Contact;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderRequest {
    private List<ProductQuantity> productsQuantityList;
    private User_Contact contact;
    private Shipment_Type shipment;
}//OrderRequest
