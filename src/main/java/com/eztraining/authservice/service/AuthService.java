package com.eztraining.authservice.service;

import com.eztraining.authservice.security.bean.AuthStudent;
import com.eztraining.authservice.bean.User;
import com.eztraining.authservice.http.Response;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private WebClient userClient;

    @Transactional
    public Response registerUser(User user,  int profileId)
    {
        Response response = null;
        try {
            //register user
            response = userService.register(user, profileId);

            //register student in microservice user-service
            if(response.isSuccess()){
                int userId = Integer.parseInt(response.getMessage());
                AuthStudent authStudent = new AuthStudent();
                authStudent.setUserId(userId);
                WebClient.ResponseSpec responseSpec = userClient.post()
                                                                .uri("/student/register")
                                                                .bodyValue(authStudent)
                                                                .retrieve();
                Response restResponse = responseSpec.bodyToMono(Response.class).block();

                if (restResponse == null || !restResponse.isSuccess()) {
                    //rollback is student registration failed
                    throw new RuntimeException("Failed to register student");
                }
            }
        } catch (RuntimeException e) {
            //set response to false and rollback if anything goes wrong in try block
            response.setSuccess(false);
            System.out.println("*********************************");
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return response;

    }

    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }

    public Claims getClaims(final String token) {
        return jwtService.getClaims(token);
    }

    public void validate(final String token) {
        jwtService.validate(token);
    }

}
