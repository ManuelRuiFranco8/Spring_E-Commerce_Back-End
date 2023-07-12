package com.psw.project1.repositories;

import com.psw.project1.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface ImageRepository extends JpaRepository<Product, Long> {

    @Query("SELECT i FROM Image i WHERE i.name = :name")
    public List<Image> findByName(String name); //returns all the images with the specified name
}//ImageRepository
