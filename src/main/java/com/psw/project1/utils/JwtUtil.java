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
        return getClaim(jwt, Claims::getSubject); //specifies the subject claim to the claim-resolver function
    }//getUserName

    private <T> T getClaim(String jwt, Function<Claims, T> claimResolver) {
        final Claims claims=getAllClaims(jwt); //takes all the claims from the token
        return claimResolver.apply(claims); //assigns all the token claims to the claim-resolver
    }//getClaim (higher oder function: it takes another function as parameter)

    private Claims getAllClaims(String jwt) { //parses the token (verifying signature) and retrieves all token's claims
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
    }//getAllClaims

    public boolean validateToken(String jwt, UserDetails uD) { //the UserDetails object represents authenticated user
        String userName=getUserName(jwt);
        if(userName.equals(uD.getUsername())) { //the username from the user and the username from the jwt match
            if(!isExpired(jwt)) { //the user's token is not expired at current time
                return true; //successful validation
            } else {
             return false; //validation failed for token expired
            }//if-else
        } else {
            return false; //validation failed for usernames mismatch
        }//if-else
    }//validateToken

    private boolean isExpired(String jwt) {
        final Date expirationDate=getExpirationDate(jwt);
        return expirationDate.before(new Date()); //returns true if the token is expired at current time
    }//isExpired

    private Date getExpirationDate(String jwt) { //fetches the expiration date from the user's token
        return getClaim(jwt, Claims::getExpiration); //specifies the expiration claim to the claim-resolver function
    }//getExpirationDate

    public String generate(UserDetails uD) { //creates authentication token from the details associated to a user
        Map<String, Object> claims=new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(uD.getUsername()) //set the token's subject claim (user's username)
                .setIssuedAt(new Date(System.currentTimeMillis())) //set the token issuance claim
                .setExpiration(new Date(System.currentTimeMillis()+VALIDITY_INTERVAL*1000)) //set the expiration claim
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) //signs the token using secret key and hashing
                .compact();
    }//generate
}//JwtUtil