package com.psw.project1.utils.messages;

import com.psw.project1.entities.User;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JwtResponse {
    private User user; //user entity associated to a specific couple <username,password">
    private String jwtToken; //authentication token generated for that user

    public JwtResponse(User user, String jwtToken) {
        this.user=user;
        this.jwtToken=jwtToken;
    }//constructor
}//JwtResponse