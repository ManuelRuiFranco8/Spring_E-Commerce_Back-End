package com.psw.project1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import com.psw.project1.entities.*;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String userName); //returns a single-element list (unique constraint on username)
    boolean existsByUsername(String userName); //returns true if a user with the same username already exists
    boolean existsByEmail(String email); //returns true if a user with the same email already exists
    boolean existsByTelephone(String number); //returns true if a user with the same cell number already exists
}//UserRepository