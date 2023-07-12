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

@RestController
public class UserController {

    @Autowired
    private UserService userServ;

    @PostConstruct //this method is executed after running the back-end server
    public void initAdmin() { //initialize the platform's administrator
        userServ.initAdmin();
    }//initAdmin

    @PostMapping({"/signIn"}) //back-end endpoint to register a new user to the platform
    public ResponseEntity registerNewUser(@RequestBody User user) { //the body is a new user in JSON format
        try {
            System.out.println(user.toString());
            User newUser=userServ.registerNewUser(user); //user successfully added
            return new ResponseEntity(newUser, HttpStatus.OK);
        } catch(AppException e) { //user cannot be added for application-specific exception
            return new ResponseEntity(e.getMsg(), HttpStatus.CONFLICT);
        } catch(IOException ioe) {
            return new ResponseEntity(ioe.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) { //user cannot be added for generic exception
            return new ResponseEntity("Incorrect data. Impossible to register the new profile", HttpStatus.BAD_REQUEST);
        }//try-catch
    }//registerNewUser

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('ADMIN')") //back-end endpoint accessible only to the administrator role
    public ResponseEntity forAdmin() {
        try {
            String msg="This URL is only accessible to an administrator";
            return new ResponseEntity(msg, HttpStatus.OK);
        } catch(Exception e) { //access attempt from user with wrong role
            return new ResponseEntity("User not authorized", HttpStatus.UNAUTHORIZED);
        }//try-catch
    }//forAdmin

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('USER')") //back-end endpoint accessible only to the user role
    public ResponseEntity forUser() {
        try {
            String msg="This URL is accessible to a generic user"; //forUser
            return new ResponseEntity(msg, HttpStatus.OK);
        } catch(Exception e) { //access attempt from user with wrong role
            return new ResponseEntity("User not authorized", HttpStatus.UNAUTHORIZED);
        }//try-catch
    }//forUser
}//UserController