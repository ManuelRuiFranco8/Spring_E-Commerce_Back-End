package com.psw.project1.services;

import com.psw.project1.configurations.JwtRequestFilter;
import com.psw.project1.entities.*;
import com.psw.project1.repositories.*;
import com.psw.project1.utils.exceptions.AppException;
import com.psw.project1.utils.exceptions.ProductAlreadyExistsException;
import com.psw.project1.utils.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository rep;

    @Autowired
    private ImageRepository repIm;

    @Autowired
    private UserRepository userRep;

    @Autowired
    private ProductInCartRepository cartRep;

    public Product addProduct(Product product) throws AppException {
        if(rep.existsByName(product.getName()) && rep.existsByVendor(product.getVendor())) {
            throw new ProductAlreadyExistsException();
        } else {
            return rep.save(product); //the new product is added to the table
        }//if-else
    }//addProduct

    public Page<Product> getAllProducts(int pageNumber, String searchKey) { //returns a paginated list with all the products of the database
        Pageable pageable=PageRequest.of(pageNumber, 8); //show by default 8 products in each page
        if(searchKey.equals("")) { //no specific research issued
            return rep.findAll(pageable);
        } else { //specific research issued
            return rep.findByNameContainingIgnoreCaseOrVendorContainingIgnoreCase(searchKey, searchKey, pageable);
        }//if-else
    }//getAllProducts

    public Product getProductDetails(Long productId) {
        return rep.findById(productId).get();
    }//getProductDetails (fetches a product from the database by specifying its id)

    public Product getProductDetailsNV(String name, String vendor) {
        return rep.findByNameAndVendor(name, vendor).get(0);
    }//getProductDetailsNV (deletes a product from the database by specifying its name and vendor

    public void deleteProduct(Long productId) {
        rep.deleteById(productId);
    }//deleteProduct (deletes a product from the database by specifying its id)

    public void deleteProductNV(String name, String vendor) {
        Long id=rep.findByNameAndVendor(name, vendor).get(0).getId();
        rep.deleteById(id);
    }//deleteProductNV (deletes a product from the database by specifying its name and vendor

    public Set<Image> uploadImage(MultipartFile[] multiFile) throws IOException {
        Set<Image> images=new HashSet<>();
        for(MultipartFile file: multiFile) {
            Image pic=new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            images.add(pic);
        }//for
        return images; //returns the extracted images
    }//uploadImage

    public Product addProduct2(Product product, MultipartFile[] multiFile) throws AppException, IOException {
        if(rep.existsByName(product.getName()) && rep.existsByVendor(product.getVendor())) {
            throw new ProductAlreadyExistsException();
        } else if(product.getName()=="" || product.getVendor()=="" ||
                  product.getPrice()<=0 || product.getQuantity()<=0) {
            throw new IOException();
        } else {
            Set<Image> images=new HashSet<>();
            Set<Image> existentImages=new HashSet<>();
            for(MultipartFile file: multiFile) {
                String name=file.getOriginalFilename();
                if(repIm.existsByName(name)) {
                    existentImages.add(repIm.findByName(name).get(0));
                } else {
                    Image pic=new Image(name, file.getContentType(), file.getBytes());
                    images.add(pic);
                }//if-else
            }//for
            product.setProductImages(images);
            Product addedProd=rep.save(product);
            for(Image i: existentImages) {
                addedProd.getProductImages().add(i);
            }//for
            return addedProd; //the new product is added to the table
        }//if-else
    }//addProduct2

    public Product updateProduct(Long productId, Product updatedProduct, MultipartFile[] multiFile) throws AppException, IOException {
        updatedProduct.setProductImages(uploadImage(multiFile));
        Optional<Product> optionalProduct=rep.findById(productId);
        if(optionalProduct.isPresent()) {
            Product existingProduct=optionalProduct.get();
            existingProduct.setDescription(updatedProduct.getDescription());
            if(updatedProduct.getPrice()>0) {
                existingProduct.setPrice(updatedProduct.getPrice());
            } else {
                throw new IOException("Invalid price specified");
            }//if-else
            if(updatedProduct.getQuantity()>0) {
                existingProduct.setQuantity(updatedProduct.getQuantity());
            } else {
                throw new IOException("Invalid quantity specified");
            }//if-else
            if(updatedProduct.getProductImages()!=null) {
                existingProduct.setProductImages(updatedProduct.getProductImages());
            }//if
            return rep.save(existingProduct);
        } else {
            throw new ProductNotFoundException();
        }//if-else
    }//updateProduct

    public List<Product> getProductsForOrder(boolean singleProduct, Long productId) {
        List<Product> list=new ArrayList<>();
        if(singleProduct && productId!=0) { //single product order
            Product product=rep.findById(productId).get();
            list.add(product);
        } else { //multiple products (cart) order
            String username=JwtRequestFilter.currentUser();
            User user=userRep.findByUsername(username).get(0);
            List<ProductInCart> cart=cartRep.findByUser(user);
            for(ProductInCart pic: cart) {
                list.add(rep.findById(pic.getProduct().getId()).get());
            }//for
            //list=cart.stream().map(x->x.getProduct()).collect(Collectors.toList());
        }//if-else
        return list;
    }//getProductsForOrder
}//ProductService
