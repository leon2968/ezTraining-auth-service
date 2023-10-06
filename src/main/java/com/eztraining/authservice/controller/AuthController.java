package com.eztraining.authservice.controller;

import com.eztraining.authservice.bean.User;
import com.eztraining.authservice.http.LoginResponse;
import com.eztraining.authservice.http.Response;
import com.eztraining.authservice.security.bean.AuthRequest;
import com.eztraining.authservice.service.AuthService;
import com.eztraining.authservice.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    //1	"ROLE_ADMIN"
    //2	"ROLE_STUDENT"
    //3	"ROLE_MANAGER"
    //4	"ROLE_INSTRUCTOR"
    @PostMapping("/register/student")
    public Response registerNewStudent(@RequestBody User user) {

        return authService.registerUser(user, 2);
    }

    @PostMapping("/register/instructor")
    public Response registerNewInstructor(@RequestBody User user) {

        return authService.registerUser(user, 4);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) {
        System.out.println(user);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authenticate.isAuthenticated()) {
            User userDto = userService.getByUsername(user.getUsername());
            String token =  authService.generateToken(userDto);
            return new LoginResponse(true, userDto.getId(), token);
        } else {
            return new LoginResponse(false, -1, null);
        }
    }

    @GetMapping("/validate")
    public LoginResponse validateToken(@RequestParam("token") String token) {
        try{
            Claims claims = authService.getClaims(token);
            int userid = claims.get("userid", Integer.class);
            return new LoginResponse(true, userid, token);

        } catch(Exception e){
            return new LoginResponse(false, -1,null);
        }

    }

    @GetMapping("/test")
    public Response test() {
        System.out.println("testing...");
        try{
            return new Response(true, "Test method is called");
        } catch(Exception e){
            return new Response(false, "Test method is called and returned false");
        }

    }
}
