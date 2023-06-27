package com.psw.project1.controllers;

import com.psw.project1.entities.*;
import com.psw.project1.services.*;
import com.psw.project1.utils.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.*;
import java.io.IOException;
import java.sql.SQLOutput;

@RestController
public class UserController {

    @Autowired
    private UserService userServ;

    @PostConstruct
    public void initAdmin() { //initialize the platform's administrator
        userServ.initAdmin();
    }//initAdmin

    @PostMapping({"/signIn"}) //method to register a new user to the platform
    public ResponseEntity registerNewUser(@RequestBody User user) { //the body is a new user in JSON format
        try {
            System.out.println(user.toString());
            User newUser=userServ.registerNewUser(user); //the request returns the newly registered user in JSON format
            return new ResponseEntity(newUser, HttpStatus.OK);
        } catch(AppException e) {
            return new ResponseEntity(e.getMsg(), HttpStatus.CONFLICT);
        } catch(IOException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch
    }//registerNewUser

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('ADMIN')") //only the administrator may be able to access this endpoint
    public ResponseEntity forAdmin() {
        try {
            String msg="This URL is only accessible to an administrator";
            return new ResponseEntity(msg, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity("User not authorized", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//forAdmin

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('USER')") //only standard user may be able to access this endpoint
    public ResponseEntity forUser() {
        try {
            String msg="This URL is accessible to a generic user"; //forUser
            return new ResponseEntity(msg, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("User not authorized", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//forUser
}//UserController