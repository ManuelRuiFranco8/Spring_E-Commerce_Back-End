package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="orders", schema="ecommerce")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="order_id", nullable=false)
    private Long id;

    @Column(name="order_buyer", nullable=false, length=100)
    private String buyer;

    @Column(name="order_address", nullable=false, length=150)
    private String shipmentAddress;

    @Column(name="order_contact", nullable=false, length=90)
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status", nullable=false, length=50)
    private Order_Status status;

    @Column(name="order_amount", nullable=false, length=50)
    private Double amount;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="order_date")
    private Date date;

    @OneToOne
    private Product boughtProduct;

    @OneToOne
    private User buyingUser;

    public Order(Order_Status status, Double amount, Product boughtProduct, User buyingUser, User_Contact contact) {
        this.buyingUser=buyingUser;
        this.buyer=buyingUser.getFirst_name()+" "+buyingUser.getLast_name();
        this.shipmentAddress=buyingUser.getAddress();
        if(contact.equals(User_Contact.EMAIL)) {
            this.contact=buyingUser.getEmail();
        } else {
            this.contact=buyingUser.getTelephone();
        }//if-else
        this.boughtProduct=boughtProduct;
        this.status=status;
        this.amount=amount;
    }//constructor

    public Order() {} //default constructor
}//Order
