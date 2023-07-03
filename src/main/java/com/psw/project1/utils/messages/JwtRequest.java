package com.psw.project1.utils.messages;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

public class JwtRequest { //the authentication token is obtained specifying username and password
    private String userName;
    private String userPassword;
}//JwtRequest