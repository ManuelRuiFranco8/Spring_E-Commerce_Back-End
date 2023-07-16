package com.psw.project1.entities;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="products", schema="ecommerce", uniqueConstraints={
    @UniqueConstraint(columnNames={"product_name", "vendor"}) //no products with the same name and vendor allowed
})//@Table
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="product_id", nullable=false)
    private Long id;

    @Column(name="vendor", nullable=false, length=50)
    private String vendor;

    @Column(name="product_name", nullable=false, length=50)
    private String name;

    @Column(name="description", nullable=false, columnDefinition="TEXT")
    private String description;

    @Column(name="price", nullable=false, columnDefinition="NUMERIC(10,2)")
    private float price;

    @Column(name="quantity", nullable=false)
    private int quantity;

    @Version //version field for locking operations
    @JsonIgnore //this field will not be part of the product's json format
    @ToString.Exclude //lombok doesn't consider this field in the toString() method
    @Column(name="version", nullable=false)
    private long version;

    @ToString.Exclude //lombok doesn't consider this field in the toString() method
    @ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.DETACH, CascadeType.MERGE,
                CascadeType.PERSIST, CascadeType.REFRESH}) //associates a product to a set of images (also empty)
    @JoinTable(name="products_images", schema="ecommerce",
               joinColumns=@JoinColumn(name="product_id"),
               inverseJoinColumns=@JoinColumn(name="image_id"))
    private Set<Image> productImages=new HashSet<>();
}//Product
