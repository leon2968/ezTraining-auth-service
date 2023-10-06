package com.eztraining.authservice.security.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthStudent {
    private String name;
    private String phone;
    private String email;
    private String address;
    private int deptId;
    private int userId;
}
