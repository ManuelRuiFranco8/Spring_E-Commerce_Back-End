package com.psw.project1.services;

import com.psw.project1.repositories.*;
import com.psw.project1.entities.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRep;

    @Transactional(readOnly=false)
    public Role createNewRole(Role role) {
        return roleRep.save(role); //adds a new role in the role table
    }//createNewRole
}//RoleService