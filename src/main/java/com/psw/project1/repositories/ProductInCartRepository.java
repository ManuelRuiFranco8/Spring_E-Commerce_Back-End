package com.psw.project1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import com.psw.project1.entities.*;
import java.util.*;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long> {
    public List<ProductInCart> findByUser(User user);
}//ProductInCartRepository
