package com.psw.project1.repositories;

import com.psw.project1.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface ImageRepository extends JpaRepository<Product, Long> {

    List<Image> findByName(String name);

    boolean existsByName(String name);
}//ImageRepository
