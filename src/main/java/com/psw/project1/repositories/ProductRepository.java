package com.psw.project1.repositories;

import com.psw.project1.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameAndVendor(String name, String vendor); //returns a single-element list (products with the
                                                                   //same name and vendor are not allowed)
    boolean existsByName(String name);
    boolean existsByVendor(String vendor);
    boolean existsByNameAndVendor(String name, String vendor); //returns true if a product with the specified name and
                                                               //vendor already exists in the database

    //Returns products searched from a search bar in pageable format
    Page<Product> findByNameContainingIgnoreCaseOrVendorContainingIgnoreCase(
            String key1, String key2, Pageable pageable); //searches in the name and vendor fields of the products
}//ProductRepository
