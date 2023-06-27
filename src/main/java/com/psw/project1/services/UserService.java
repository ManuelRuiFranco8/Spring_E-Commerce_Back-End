package com.psw.project1.services;

import com.psw.project1.repositories.*;
import com.psw.project1.entities.*;
import com.psw.project1.utils.exceptions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;

import java.io.IOException;
import java.util.*;
import static com.psw.project1.entities.User_Roles.ADMIN;
import static com.psw.project1.entities.User_Roles.USER;

@Service
public class UserService {

    @Autowired
    private UserRepository userRep;
    @Autowired
    private RoleRepository roleRep;
    @Autowired
    private PasswordEncoder encoder;

    //@Transactional
    public User registerNewUser(User user) throws AppException, IOException { //registers a new user (NOT ADMIN)
        if(user.getUsername().equals("") || user.getFirst_name().equals("") || user.getLast_name().equals("") ||
           user.getPassword().equals("") || user.getAddress().equals("") || user.getTelephone().equals("") ||
           user.getEmail().equals("")) {
            throw new IOException("Incorrect data. Impossible to register the profile");
        } else if(userRep.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyUsedException();
        } else if(userRep.existsByEmail(user.getEmail())) {
            throw new MailAlreadyUsedException();
        } else if(userRep.existsByTelephone(user.getTelephone())) {
            throw new TelephoneNumberAlreadyUsedException();
        } else {
            user.setPassword(getEncoder(user.getPassword())); //the password is saved in encrypted text
            Set<Role> roles=new HashSet<>();
            roles.add(roleRep.findByType(USER).get(0));
            user.setRoles(roles); //assigns the generic user role
            return userRep.save(user); //the new user is added to the table
        }//else
    }//registerNewUser

    public void initAdmin() { //create a single default admin for the platform and stores it in the database
        if(!userRep.existsByUsername("Francesco99Admin")) {
            User admin=new User();
            admin.setUsername("Francesco99Admin");
            admin.setFirst_name("Francesco");
            admin.setLast_name("Mazzei");
            admin.setEmail("mzzfnc99l26c352h@studenti.unical.it");
            admin.setAddress("Via Alberto Savinio n° 9, Quattromiglia di Rende (CS) 87036");
            admin.setTelephone("3323855058");
            admin.setPassword(getEncoder("Milan$tich88")); //the password is saved in encrypted text
            Set<Role> roles=new HashSet<>();
            roles.add(roleRep.findByType(ADMIN).get(0));
            admin.setRoles(roles); //assigns the admin role
            userRep.save(admin);
        }//if
    }//initAdmin*/

    public String getEncoder(String password) {
        return encoder.encode(password);
    }//getEncoder

    //@Transactional
    public List<User> getAllUsers() {
        return userRep.findAll();
    }//getAllUsers
}//UserService