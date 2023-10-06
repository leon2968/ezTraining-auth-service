package com.eztraining.authservice.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends Response{

    private int userId;
    private String token;

    public LoginResponse(boolean success, int userId, String token){
        super(success);
        this.userId = userId;
        this.token = token;
    }

}
