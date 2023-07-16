package com.psw.project1.repositories;

import com.psw.project1.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import javax.persistence.LockModeType;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    List<Product> findByNameAndVendor(String name, String vendor); //returns a single-element list (products with the
                                                                   //same name and vendor are not allowed)
    @Lock(LockModeType.OPTIMISTIC)
    boolean existsByNameAndVendor(String name, String vendor); //returns true if a product with the specified name and
                                                               //vendor already exists in the database

    //Returns products searched from a search bar in pageable format
    @Lock(LockModeType.OPTIMISTIC)
    Page<Product> findByNameContainingIgnoreCaseOrVendorContainingIgnoreCase(
            String key1, String key2, Pageable pageable); //searches in the name and vendor fields of the products

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findById(Long aLong);
}//ProductRepository
