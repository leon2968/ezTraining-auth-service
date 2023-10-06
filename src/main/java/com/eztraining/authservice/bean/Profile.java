package com.eztraining.authservice.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="PROFILE")
public class Profile implements GrantedAuthority {
    @Id
    private int id;

    private String type;

    public Profile(int id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return type;
    }

    public String toString(){
        return this.type;
    }
}

