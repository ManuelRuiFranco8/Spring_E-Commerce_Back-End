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

    @PostMapping() //adds a new row in the role table (it will not have a front-end endpoint)
    public ResponseEntity createNewRole(@RequestBody Role role) { //the body is a new role in JSON format
        try {
            Role newRole=roleServ.createNewRole(role);
            return new ResponseEntity(newRole, HttpStatus.OK); //role successfully added
        } catch(Exception e) { //role cannot be added
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch
    }//createNewRole
}//RoleController