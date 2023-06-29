package com.psw.project1.controllers;

import com.psw.project1.entities.*;
import com.psw.project1.services.*;
import com.psw.project1.utils.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService serv;

    //method to add a new product in the platform
    @PreAuthorize("hasRole('ADMIN')") //only the administrator may be able to access this endpoint
    @PostMapping(value={"/addProduct"}, consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity addProduct(@RequestPart("product") Product product,
                                     @RequestPart("imageFile") MultipartFile[] file) {
                                     //the body is a new product in JSON format and a file of images
        try{
            //Set<Image> images=serv.uploadImage(file);
            //product.setProductImages(images);
            Product newProd=serv.addProduct2(product, file);
            return new ResponseEntity(newProd, HttpStatus.OK); //the request returns the newly added product in JSON format
        } catch(AppException e) {
            System.out.println(e.getMsg());
            return new ResponseEntity(e.getMsg(), HttpStatus.CONFLICT);
        } catch(Exception e) {
            System.out.println("Incorrect data. Impossible to add product to the database.");
            return new ResponseEntity("Incorrect data. Impossible to add product to the database.", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//addNewProduct

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value={"/updateProduct"}, consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updateProduct(@RequestParam("productId") long productId,
                                        @RequestPart("product") Product updatedProduct,
                                        @RequestPart("imageFile") MultipartFile[] file) {
        try {
            System.out.println(productId);
            Product updatedProd=serv.updateProduct(productId, updatedProduct, file);
            return new ResponseEntity<>(updatedProd, HttpStatus.OK);
        } catch (AppException e) {
            System.out.println(e.getMsg());
            return new ResponseEntity<>(e.getMsg(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Error updating product. Please check the request data.", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//updateProduct

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping({"/getAllProducts"})
    public Page<Product> getAllProducts(@RequestParam(defaultValue="0") int pageNumber,
                                        @RequestParam(defaultValue="") String searchKey) {
        return serv.getAllProducts(pageNumber, searchKey);
    }//getAllProducts

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping({"/getProductDetails"})
    public ResponseEntity getProductDetails(@RequestParam("productId") long productId) { //or @PathVariable?
        try {
            Product prod=serv.getProductDetails(productId);
            return new ResponseEntity(prod, HttpStatus.OK);
        } catch(IndexOutOfBoundsException e) {
            return new ResponseEntity<>("Product not present in the database", HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }//deleteProduct

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping({"/getProductDetailsNV"})
    public ResponseEntity getProductDetailsNV(@RequestParam("productName") String name, @RequestParam("productVendor") String vendor) {
        try {
            Product prod=serv.getProductDetailsNV(name,vendor);
            return new ResponseEntity(prod, HttpStatus.OK);
        } catch(IndexOutOfBoundsException e) {
            return new ResponseEntity<>("Product not present in the database", HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }//deleteProductNV

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/deleteProduct"})
    public void deleteProduct(@RequestParam("productId") long productId) {
        serv.deleteProduct(productId);
    }//deleteProduct

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/deleteProductNV"})
    public void deleteProductNV(@RequestParam("productName") String name, @RequestParam("productVendor") String vendor) {
        serv.deleteProductNV(name, vendor);
    }//deleteProductNV
}//ProductController
