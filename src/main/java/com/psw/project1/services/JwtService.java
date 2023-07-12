package com.psw.project1.services;

import com.psw.project1.entities.*;
import com.psw.project1.repositories.*;
import com.psw.project1.utils.*;
import com.psw.project1.utils.messages.JwtRequest;
import com.psw.project1.utils.messages.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;
import java.util.*;

@Service
public class JwtService implements UserDetailsService { //the UserDetailsService interface loads suser-specific data
                                                        //for authentication

    @Autowired
    private UserRepository userRep;
    @Autowired
    private JwtUtil util;
    @Autowired
    private AuthenticationManager manager;

    public JwtResponse createToken(JwtRequest request) throws Exception {
        String userName=request.getUserName();
        String userPassword=request.getUserPassword();
        authenticate(userName, userPassword); //checks if user credentials associated with the request are valid
        final UserDetails uD=loadUserByUsername(userName); //retrieves user details
        String token=util.generate(uD); //token generated after authentication
        User user=userRep.findByUsername(userName).get(0); //user associated with the request
        return new JwtResponse(user, token); //contains the authenticated user and his associated token
    }//createToken

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword)); //auth success
        } catch(DisabledException e) { //unsuccessful authentication
           throw new Exception("The user is disabled");
        } catch(BadCredentialsException e) { //unsuccessful authentication
            throw new Exception("Bad credentials form the user");
        }//try-catch
    }//authenticate

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user=userRep.findByUsername(userName).get(0); //remember: unique constraint over username
        if(user!=null) { //a user associated with the specified username exists
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    getAuthorities(user)); //creates user details object with authentication-specific data
        } else { //user details cannot be extracted
            throw new UsernameNotFoundException("This username is not valid!");
        }//if-else
    }//loadUserByUsername

    private Set getAuthorities(User user) { //gets the roles associated to the specified user
        Set authorities=new HashSet();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getType())); //create authority for each role
        });//role stream
        return authorities;
    }//getAuthorities
}//JwtService