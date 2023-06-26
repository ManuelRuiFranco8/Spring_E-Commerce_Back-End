package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="roles", schema="ecommerce")
public class Role { //Defines the user's role inside the E-commerce platform

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="role_type", nullable=false, length=50)
    private User_Roles type;

    @Column(name="description", nullable=true, length=150)
    private String description;
}//Role