package com.psw.project1.repositories;

import com.psw.project1.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface ImageRepository extends JpaRepository<Product, Long> {

    List<Image> findByName(String name); //returns all the images with the specified name

    boolean existsByName(String name); //returns true if an image with the specified name already exists
}//ImageRepository
