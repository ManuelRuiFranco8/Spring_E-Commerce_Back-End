package com.psw.project1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import com.psw.project1.entities.*;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String userName); //returns a single-element list (unique constraint on username)
    List<User> findByEmail(String email); //returns a single-element list (unique constraint on email)
    List<User> findByTelephone(String number); //returns a single-element list (unique constraint on cell number)

    //List<User> findByFirst_nameAndLast_name(String firstName, String lastName);
    List<User> findByAddress(String address);
    boolean existsByUsername(String userName); //returns true if an user with the same username already exists
    boolean existsByEmail(String email); //returns true if an user with the same email already exists
    boolean existsByTelephone(String number); //returns true if an user with the same cell number already exists
}//UserRepository