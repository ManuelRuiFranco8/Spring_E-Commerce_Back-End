package com.psw.project1.entities;

import javax.persistence.*;
import com.psw.project1.utils.enums.Order_Status;
import com.psw.project1.utils.enums.Shipment_Type;
import com.psw.project1.utils.enums.User_Contact;
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

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="shipment_date")
    private Date ShipmentDate;

    @OneToOne
    private Product boughtProduct;

    @OneToOne
    private User buyingUser;

    public Order(Order_Status status, Double amount, Product boughtProduct, User buyingUser, User_Contact contact,
                 Shipment_Type shipment) {
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

        Date currentDate=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(currentDate);
        if(shipment.equals(Shipment_Type.PREMIUM)) {
            calendar.add(Calendar.DAY_OF_MONTH, 5);
            this.ShipmentDate=calendar.getTime();
        } else if(shipment.equals(Shipment_Type.STANDARD)) {
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            this.ShipmentDate=calendar.getTime();
        }//if-else
    }//constructor

    public Order() {} //default constructor
}//Order
