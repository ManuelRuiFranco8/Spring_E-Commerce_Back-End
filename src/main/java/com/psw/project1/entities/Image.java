package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="images", schema="ecommerce",
        uniqueConstraints={@UniqueConstraint(columnNames="image_name")})
public class Image {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="image_id", nullable=false)
    private Long id;

    @Column(name="image_name", nullable=false, length=50)
    private String name;

    @Column(name="image_type", nullable=false, length=50)
    private String type;

    @Column(name="picture_content", nullable=false, length=500000)
    private byte[] picture;

    public Image(String name, String type, byte[] picture) {
        this.name=name;
        this.type=type;
        this.picture=picture;
    }//constructor(parameters)

    public Image() {}//constructor(no parameters)
}//Image
