package com.psw.project1.entities;

import javax.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name="users", schema="ecommerce", uniqueConstraints={
        @UniqueConstraint(columnNames="username"),
        @UniqueConstraint(columnNames="telephone"),
        @UniqueConstraint(columnNames="email"),
        @UniqueConstraint(columnNames="user_password"),
})
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id", nullable=false)
    private Long id;

    @Column(name="username", nullable=false, length=50)
    private String username;

    @Column(name="first_name", nullable=false, length=50)
    private String first_name;

    @Column(name="last_name", nullable=false, length=50)
    private String last_name;

    @Column(name="telephone", nullable=false, length=20)
    private String telephone;

    @Column(name="email", nullable=false, length=90)
    private String email;

    @Column(name="address", nullable=true, length=150)
    private String address;

    @Column(name="user_password", nullable=false, columnDefinition="TEXT")
    private String password;

    //cascade=CascadeType.ALL
    @ManyToMany(fetch=FetchType.EAGER) //associate each user to at least a role in the platform
    @JoinTable(name="users_roles", schema="ecommerce",
            joinColumns=@JoinColumn(name="user_id", referencedColumnName="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_type",referencedColumnName="role_type"))
    private Set<Role> roles=new HashSet<>(); //a single user can possibly have more than one role
}//User