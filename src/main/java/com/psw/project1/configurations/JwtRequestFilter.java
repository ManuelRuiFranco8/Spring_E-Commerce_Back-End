package com.psw.project1.configurations;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.psw.project1.services.*;
import com.psw.project1.utils.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.*;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil util;
    @Autowired
    private JwtService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String header=request.getHeader("Authorization"); //takes header from HTTP request
        String jwtToken=null;
        String userName=null;
        if(header!=null && header.startsWith("Bearer ")) { //retrives jwt token from header
            jwtToken=header.substring(7); //starts the index after the "Bearer "
            try {
                userName=util.getUserName(jwtToken); //retrive username from the token
            } catch(IllegalArgumentException e) {
                System.out.println("Unable to retrieve authentication token");
            } catch(ExpiredJwtException e) {
                System.out.println("Your authentication token is expired");
            }//try-catch
        } else {
            System.out.println("Your authentication token does not start with 'Bearer '");
        }//if-else
        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails uD=service.loadUserByUsername(userName); //gets user details from token
            if(util.validateToken(jwtToken, uD)) { //if the token is valid
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                                                                    uD, null, uD.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }//if
        }//if
        filterChain.doFilter(request, response);
    }//doFilterInternal (this filter intercepts the user's request to perform authentication)
}//JwtRequestFilter