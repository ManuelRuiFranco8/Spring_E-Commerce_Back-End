package com.psw.project1.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.*;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY="P$W2223$F"; //secret key to parse the token
    private static final int VALIDITY_INTERVAL=3600*5; //interval of validity for the authentication token (5 hours)

    public String getUserName(String jwt) { //obtain username from jwt token
        return getClaim(jwt, Claims::getSubject);
    }//getUserName

    private <T> T getClaim(String jwt, Function<Claims, T> claimResolver) {
        final Claims claims=getAllClaims(jwt);
        return claimResolver.apply(claims);
    }//getClaim (higher oder function: it takes another function as parameter)

    private Claims getAllClaims(String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
    }//getAllClaims

    public boolean validateToken(String jwt, UserDetails uD) {
        String userName=getUserName(jwt);
        if(userName.equals(uD.getUsername())) { //the username from the user and the username from the jwt matches
            if(!isExpired(jwt)) { //the user's token is not expired at current time
                return true;
            } else {
             return false;
            }//if-else
        } else {
            return false;
        }//if-else
    }//validateToken

    private boolean isExpired(String jwt) {
        final Date expirationDate=getExpirationDate(jwt);
        return expirationDate.before(new Date()); //return true if the token is expired at current time
    }//isExpired

    private Date getExpirationDate(String jwt) {
        return getClaim(jwt, Claims::getExpiration); //gets the expiration date from the user's token
    }//getExpirationDate

    public String generate(UserDetails uD) { //create authentication token from the details associated to a user
        Map<String, Object> claims=new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(uD.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) //token's creation time
                .setExpiration(new Date(System.currentTimeMillis()+VALIDITY_INTERVAL*1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) //hashing
                .compact();
    }//generate
}//JwtUtil