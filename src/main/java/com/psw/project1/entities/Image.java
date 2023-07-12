package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="images", schema="ecommerce", uniqueConstraints={
        @UniqueConstraint(columnNames="image_name")})
public class Image {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="image_id", nullable=false)
    private Long id;

    @Column(name="image_name", nullable=false, length=50)
    private String name;

    @Column(name="image_type", nullable=false, length=50)
    private String type;
    @ToString.Exclude //lombok doesn't consider this field in the toString() method
    @Column(name="picture_content", nullable=false, length=500000)
    private byte[] picture; //images of various format are stored in the database as byte array

    public Image(String name, String type, byte[] picture) {
        this.name=name;
        this.type=type;
        this.picture=picture;
    }//constructor(parameters)

    public Image() {}//default constructor(no parameters)
}//Image