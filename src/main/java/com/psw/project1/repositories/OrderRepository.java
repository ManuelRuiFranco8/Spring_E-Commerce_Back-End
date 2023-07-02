package com.psw.project1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import com.psw.project1.entities.*;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long>{
}//OrderRepository
