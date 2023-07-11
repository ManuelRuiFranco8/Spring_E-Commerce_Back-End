package com.psw.project1.repositories;

import com.psw.project1.utils.enums.Order_Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.*;
import com.psw.project1.entities.*;
import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

    public List<Order> findByBuyingUser(User user); //returns all the order associated to the specified user

    @Query("SELECT o FROM Order o WHERE o.buyingUser = :user AND o.status = :status")
    public List<Order> findByStatus(User user, Order_Status status); //returns all the order associated to the
                                                                     //specified user and with the specified status
}//OrderRepository
