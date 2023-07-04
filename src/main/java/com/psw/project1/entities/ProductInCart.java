package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="productsInCart", schema="ecommerce")
public class ProductInCart {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="inCart_id", nullable=false)
    private Long id;

    @OneToOne
    private Product product;

    @OneToOne
    private User user;

    public ProductInCart(Product product, User user) {
        this.product=product;
        this.user=user;
    }//constructor

    public ProductInCart() {}//default constructor
}//ProductInCart
