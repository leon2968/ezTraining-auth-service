package com.eztraining.authservice.security;

import java.io.IOException;
import java.io.PrintWriter;


import com.eztraining.authservice.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;


public class SecurityUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendResponse(HttpServletResponse response, int status, String message, Exception exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(new Response(exception == null, status, message)));
        if(exception!=null)
            exception.printStackTrace();
        response.setStatus(status);
        writer.flush();
        writer.close();
    }

}