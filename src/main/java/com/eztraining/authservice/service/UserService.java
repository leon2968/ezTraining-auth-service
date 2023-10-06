package com.eztraining.authservice.service;

import com.eztraining.authservice.bean.Profile;
import com.eztraining.authservice.bean.User;
import com.eztraining.authservice.dao.UserDao;
import com.eztraining.authservice.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User getById(int id){
        Optional<User> op = userDao.findById(id);
        return op.orElse(null);
    }

    public User getByUsername(String username){
        Optional<User> op = userDao.findByUsername(username);
        return op.orElse(null);
    }

    public List<User> getAllUsers(){
        return userDao.findAll();
    }

    /**
     * Still need to implement the corresponding student/instructor registration events in the other microservice
     * @param user
     * @param profileId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Response register(User user, int profileId) {
        if(isUsernameUsed(user)){
            return new Response(false);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Profile> profiles = new ArrayList<Profile>();

        profiles.add(new Profile(profileId));
        user.setProfiles(profiles);

        userDao.save(user);
        int userId = user.getId();
        return new Response(true, Integer.toString(userId));
    }

    boolean isUsernameUsed(User user){
        User existingUser= userDao.findByUsername(user.getUsername()).get();
        if(existingUser == null)
            return false;
        return true;
    }

    public Response changePassword(User user, Authentication authentication) {
        if(user.getUsername().equals(authentication.getName()) || isProfile("admin", authentication.getAuthorities())) {
            User u = userDao.findByUsername(user.getUsername()).get();
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.save(u);
        }else {
            //TODO: Not authorize to update password if not current loggedin user or admin.
            return new Response(false);
        }
        return new Response(true);
    }

    /**
     * method to check if user has a profile of Role_{role}
     * @param role : can be any of the following, "instructor","student","manager","admin"
     * @param profiles : user's profiles
     * @return
     */
    public boolean isProfile(String role, Collection<? extends GrantedAuthority> profiles) {
        boolean isProfile = false;
        for(GrantedAuthority profle: profiles) {
            if(profle.getAuthority().equals("ROLE_"+"role")) {
                isProfile = true;
            }
        };
        return isProfile;
    }

    public Response deleteUser(int id) {
        if(userDao.findById(id).get() != null) {
            userDao.deleteById(id);
            return new Response(true);
        }else {
            return new Response(false, "User is not found!");
        }
    }

    public List<Profile> getProfiles(User user){
        Optional<User> existingUser = userDao.findByUsername(user.getUsername());;
        List<Profile> profiles = null;
        if(existingUser.isPresent()) {
            profiles = existingUser.get().getProfiles();
        }
        return profiles;
    }
}
