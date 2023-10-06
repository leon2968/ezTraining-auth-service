package com.eztraining.authservice.bean;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="APP_USER")
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "USER_SEQ", allocationSize = 1)
    @GeneratedValue(generator="user_seq_gen", strategy = GenerationType.AUTO)
    private int id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "USER_PROFILE",

            // from middle table point of view
            joinColumns = {
                    @JoinColumn(name="USER_ID", referencedColumnName = "ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name="PROFILE_ID", referencedColumnName = "ID")
            })
    private List<Profile> profiles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return profiles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
