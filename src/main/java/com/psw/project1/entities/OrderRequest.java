package com.psw.project1.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderRequest {
    private List<ProductQuantity> productsQuantityList;
    private User_Contact contact;
}//OrderRequest
