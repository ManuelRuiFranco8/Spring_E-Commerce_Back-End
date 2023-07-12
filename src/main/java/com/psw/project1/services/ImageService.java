package com.psw.project1.services;

import com.psw.project1.entities.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class ImageService {

    public Set<Image> uploadImage(MultipartFile[] multiFile) throws IOException {
        Set<Image> images=new HashSet<>();
        for(MultipartFile file: multiFile) { //extracts images one by one from the MultipartFile[]
            Image pic=new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            images.add(pic);
        }//for
        return images; //returns the images extracted from an add product or update product request
    }//uploadImage
}//ImageService
