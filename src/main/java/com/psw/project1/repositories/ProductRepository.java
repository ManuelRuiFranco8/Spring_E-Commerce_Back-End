package com.psw.project1.repositories;

import com.psw.project1.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String name); //search for a product using keywords
    List<Product> findByVendor(String vendor); //search for all the products of that vendor

    //Two products with the same name and vendor cannot exist
    List<Product> findByNameAndVendor(String name, String vendor);
    boolean existsByName(String name);
    boolean existsByVendor(String vendor);

    Page<Product> findAll(Pageable pageable);

    //Returns products searched from a search bar
    Page<Product> findByNameContainingIgnoreCaseOrVendorContainingIgnoreCase(
            String key1, String key2, Pageable pageable);
}//ProductRepository
