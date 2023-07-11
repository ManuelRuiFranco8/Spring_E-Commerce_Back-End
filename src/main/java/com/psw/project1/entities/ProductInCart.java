package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="productsInCart", schema="ecommerce")
public class ProductInCart { //this entity represents a product currently placed in a user's cart

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="inCart_id", nullable=false)
    private Long id;

    @OneToOne
    private Product product; //a product in cart basically consists into a <product, user> couple

    @OneToOne
    private User user; //a product in cart basically consists into a <product, user> couple

    public ProductInCart(Product product, User user) {
        this.product=product;
        this.user=user;
    }//constructor(parameters)

    public ProductInCart() {}//default constructor(no parameters)
}//ProductInCart
