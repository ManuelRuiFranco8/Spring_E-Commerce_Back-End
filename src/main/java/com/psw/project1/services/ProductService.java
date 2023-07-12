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

    public Product addProduct(Product product, MultipartFile[] multiFile) throws AppException, IOException {
        if(rep.existsByNameAndVendor(product.getName(), product.getVendor())) {
            throw new ProductAlreadyExistsException();
        } else if(product.getName()=="" || product.getVendor()=="" ||
                product.getPrice()<=0 || product.getQuantity()<=0) { //some necessary fields are unspecified
            throw new IOException("Please correctly specify all fields to add the product.");
        } else { //the product can be added
            Set<Image> images=new HashSet<>(); //set of images in the request NOT present in the db
            Set<Image> existentImages=new HashSet<>(); //set of images in the request already present in the db
            for(MultipartFile file: multiFile) {
                String name=file.getOriginalFilename();
                //System.out.println(name); (debugging)
                if(repIm.findByName(name).size()!=0) { //if the image is already present in the db
                    existentImages.add(repIm.findByName(name).get(0));
                } else { //if the image does not exist
                    Image pic=new Image(name, file.getContentType(), file.getBytes());
                    images.add(pic);
                }//if-else
            }//for
            //System.out.println(images); (debugging)
            //System.out.println(existentImages); (debugging)
            product.setProductImages(images); //associates the new images with the new product
            for(Image i: existentImages) { //associates already existing images with the new product
                product.getProductImages().add(i);
            }//for
            Product addedProd=rep.save(product); //adds the new product to the db and the new images in cascade
            return addedProd;
        }//if-else
    }//addProduct

    public Page<Product> getAllProducts(int pageNumber, String searchKey) {
        Pageable pageable=PageRequest.of(pageNumber, 8); //shows by default 8 products in each page
        if(searchKey.equals("")) { //no specific research issued
            return rep.findAll(pageable);
        } else { //specific research issued
            return rep.findByNameContainingIgnoreCaseOrVendorContainingIgnoreCase(searchKey, searchKey, pageable);
        }//if-else
    }//getAllProducts (returns a paginated list with all the products of the database)

    public Product getProductDetails(Long productId) {
        return rep.findById(productId).get();
    }//getProductDetails (fetches a product from the database by specifying its id)

    public Product getProductDetailsNV(String name, String vendor) {
        return rep.findByNameAndVendor(name, vendor).get(0);
    }//getProductDetailsNV (fetches a product from the database by specifying its name and vendor)

    public void deleteProduct(Long productId) {
        rep.deleteById(productId);
    }//deleteProduct (deletes a product from the database by specifying its id)

    public void deleteProductNV(String name, String vendor) {
        Long id=rep.findByNameAndVendor(name, vendor).get(0).getId();
        rep.deleteById(id);
    }//deleteProductNV (deletes a product from the database by specifying its name and vendor

    public Set<Image> uploadImage(MultipartFile[] multiFile) throws IOException {
        Set<Image> images=new HashSet<>();
        for(MultipartFile file: multiFile) { //extracts images one by one from the MultipartFile[]
            Image pic=new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            images.add(pic);
        }//for
        return images; //returns the images extracted from an add product or update product request
    }//uploadImage

    public Product updateProduct(Long productId, Product updatedProduct, MultipartFile[] multiFile)
                                 throws AppException, IOException {
        updatedProduct.setProductImages(uploadImage(multiFile)); //extracts the images from the update request
        Optional<Product> optionalProduct=rep.findById(productId); //fetches product to update (part 1)
        if(optionalProduct.isPresent()) {
            Product existingProduct=optionalProduct.get(); //fetches product to update (part 2)
            existingProduct.setDescription(updatedProduct.getDescription()); //update product's description
            if(updatedProduct.getPrice()>0) { //if the new price is valid
                existingProduct.setPrice(updatedProduct.getPrice()); //upadte product's price
            } else {
                throw new IOException("Invalid price specified");
            }//if-else
            if(updatedProduct.getQuantity()>0) { //if the new quantity is valid
                existingProduct.setQuantity(updatedProduct.getQuantity()); //update product's quantity
            } else {
                throw new IOException("Invalid quantity specified");
            }//if-else
            System.out.println(updatedProduct.getProductImages());
            System.out.println(existingProduct.getProductImages());
            if(updatedProduct.getProductImages()!=null) { //change product images
                if(updatedProduct.getProductImages().size()==0) { //no images to add/all images deleted
                    existingProduct.setProductImages(updatedProduct.getProductImages());
                } else { //images to add
                    Set<Image> imagesToAdd=new HashSet<>();
                    for(Image i: updatedProduct.getProductImages()) {
                        if(repIm.findByName(i.getName()).size()!=0) { //if the image is already present in the db
                           imagesToAdd.add(repIm.findByName(i.getName()).get(0));
                        } else { //if the image is new
                            imagesToAdd.add(i);
                        }//if-else
                    }//for
                    existingProduct.setProductImages(imagesToAdd);
                }//if-elese
            }//if
            return rep.save(existingProduct);
        } else {
            throw new ProductNotFoundException();
        }//if-else
    }//updateProduct

    public List<Product> getProductsForOrder(boolean singleProduct, Long productId) {
        List<Product> list=new ArrayList<>();
        if(singleProduct && productId!=0) { //single product order
            Product product=rep.findById(productId).get(); //fetch the product
            list.add(product);
        } else { //multiple products (cart) order
            String username=JwtRequestFilter.currentUser();
            User user=userRep.findByUsername(username).get(0); //fetches current user
            List<ProductInCart> cart=cartRep.findByUser(user);
            for(ProductInCart pic: cart) { //fetches all the products in the cart of the current user
                list.add(rep.findById(pic.getProduct().getId()).get());
            }//for
        }//if-else
        return list;
    }//getProductsForOrder
}//ProductService
