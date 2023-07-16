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

    //back-end endpoint to add a new product in the platform
    @PreAuthorize("hasRole('ADMIN')") //only the administrator may be able to access this endpoint
    @PostMapping(value={"/addProduct"}, consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity addProduct(@RequestPart("product") Product product,
                                     @RequestPart("imageFile") MultipartFile[] file) {
        try{
            Product newProd=serv.addProduct(product, file);
            return new ResponseEntity(newProd, HttpStatus.OK); //new product successfully added
        } catch(AppException e) { //new product cannot be added for application-specific exception
            System.out.println(e.getMsg());
            return new ResponseEntity(e.getMsg(), HttpStatus.CONFLICT);
        } catch(IOException ioe) {
            return new ResponseEntity(ioe.getMessage(),HttpStatus.BAD_REQUEST);
        } catch(Exception e) { //new product cannot be added for generic exception
            System.out.println("Incorrect data. Impossible to add product to the database.");
            return new ResponseEntity("Incorrect data. Impossible to add product to the database.",
                                       HttpStatus.BAD_REQUEST);
        }//try-catch
    }//addNewProduct

    //back-end endpoint update an already existing product
    @PreAuthorize("hasRole('ADMIN')") //only the administrator may be able to access this endpoint
    @PutMapping(value={"/updateProduct"}, consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updateProduct(@RequestParam("productId") long productId,
                                        @RequestPart("product") Product updatedProduct,
                                        @RequestPart("imageFile") MultipartFile[] file) {
        try {
            System.out.println(productId);
            Product updatedProd=serv.updateProduct(productId, updatedProduct, file);
            return new ResponseEntity<>(updatedProd, HttpStatus.OK); //product successfully updated
        } catch (AppException e) { //product cannot be updated for application-specific exception
            System.out.println(e.getMsg());
            return new ResponseEntity<>(e.getMsg(), HttpStatus.NOT_FOUND);
        } catch (IOException e) { //product cannot be updated for generic exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch
    }//updateProduct

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") //accessible to both roles
    @GetMapping({"/getAllProducts"}) //back-end endpoint to get all products in a paginated format
    public Page<Product> getAllProducts(@RequestParam(defaultValue="0") int pageNumber,
                                        @RequestParam(defaultValue="") String searchKey) {
        return serv.getAllProducts(pageNumber, searchKey);
    }//getAllProducts

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") //accessible to both roles
    @GetMapping({"/getProductDetails"}) //back-end endpoint to get a specific product (specifying product id)
    public ResponseEntity getProductDetails(@RequestParam("productId") long productId) {
        try {
            Product prod=serv.getProductDetails(productId);
            return new ResponseEntity(prod, HttpStatus.OK); //product successfully fetched
        } catch(IndexOutOfBoundsException e) { //product cannot be fetched because not present
            return new ResponseEntity<>("Product not present in the database", HttpStatus.NOT_FOUND);
        } catch(Exception e) { //product cannot be fetched for a generic exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }//try-catch
    }//deleteProduct

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")//accessible to both roles
    @GetMapping({"/getProductDetailsNV"}) //back-end endpoint to get a specific product (product name and vendor)
    public ResponseEntity getProductDetailsNV(@RequestParam("productName") String name,
                                              @RequestParam("productVendor") String vendor) {
        try {
            Product prod=serv.getProductDetailsNV(name,vendor);
            return new ResponseEntity(prod, HttpStatus.OK); //product successfully fetched
        } catch(IndexOutOfBoundsException e) { //product cannot be fetched because not present
            return new ResponseEntity<>("Product not present in the database", HttpStatus.NOT_FOUND);
        } catch(Exception e) { //product cannot be fetched for a generic exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }//try-catch
    }//deleteProductNV

    @PreAuthorize("hasRole('ADMIN')") //only the administrator may be able to access this endpoint
    @DeleteMapping({"/deleteProduct"}) //back-end endpoint to delete a product (specifying product id)
    public ResponseEntity deleteProduct(@RequestParam("productId") long productId) {
        try {
            serv.deleteProduct(productId);
            return new ResponseEntity(HttpStatus.OK);
        } catch(IOException ioe) {
            return new ResponseEntity(ioe.getMessage(), HttpStatus.CONFLICT);
        }//try-catch
    }//deleteProduct

    @PreAuthorize("hasRole('ADMIN')") //only the administrator may be able to access this endpoint
    @DeleteMapping({"/deleteProductNV"}) //back-end endpoint to delete a product (specifying product name and vendor)
    public ResponseEntity deleteProductNV(@RequestParam("productName") String name, @RequestParam("productVendor") String vendor) {
        try {
            serv.deleteProductNV(name, vendor);
            return new ResponseEntity(HttpStatus.OK);
        } catch(IOException ioe) {
            return new ResponseEntity(ioe.getMessage(), HttpStatus.CONFLICT);
        }//try-catch
    }//deleteProductNV

    @PreAuthorize("hasRole('USER')") //only the user role may be able to access this endpoint
    @GetMapping({"/getProductsForOrder/{singleProduct}/{productId}"})
    public List<Product> getProductsForOrder(@PathVariable(name="singleProduct") boolean singleProduct,
                                             @PathVariable(name="productId") Long productId) {
        List<Product> list=serv.getProductsForOrder(singleProduct, productId);
        System.out.println(list);
        return list;
    }//getProductsForOrder (back-end endpoint to fetch all products associated to current user's order)
}//ProductController
