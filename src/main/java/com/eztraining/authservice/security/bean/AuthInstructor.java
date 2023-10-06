package com.eztraining.authservice.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthInstructor {
    private String name;
    private String phone;
    private String email;
    private String address;
    private int dept_id;
    private int user_id;
}