package com.psw.project1.controllers;

import com.psw.project1.entities.*;
import com.psw.project1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private JwtService service;

    @CrossOrigin
    @PostMapping({"/authenticate"})
    public ResponseEntity obtainToken(@RequestBody JwtRequest request) {
        try {
            JwtResponse token=service.createToken(request);
            return new ResponseEntity(token, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity("Bad credentials from the user", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//obtainToken
}//AuthController