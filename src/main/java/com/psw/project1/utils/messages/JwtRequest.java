package com.psw.project1.utils.messages;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

public class JwtRequest { //user's request issued to obtain the authentication token
    private String userName;
    private String userPassword;
}//JwtRequest