package com.psw.project1.controllers;

import com.psw.project1.entities.*;
import com.psw.project1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/createNewRole"})
public class RoleController {

    @Autowired
    private RoleService roleServ;

    @PostMapping() //method to add a new row in the role table
    public ResponseEntity createNewRole(@RequestBody Role role) { //the body of the request is a new role in JSON format
        try {
            Role newRole=roleServ.createNewRole(role);
            return new ResponseEntity(newRole, HttpStatus.OK); //the request returns the newly added role in JSON format
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch
    }//createNewRole
}//RoleController