package com.eztraining.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableMethodSecurity //to set hasAuthority on method levels
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccessDeniedHandler accessDeniedHandlerImpl;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPointImpl;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandlerImpl;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandlerImpl;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandlerImpl;

    //by type, then by name
    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(c -> {
            c.disable();
        });

        http.cors(c -> {
            c.configurationSource(corsConfigurationSource());
        });

        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/auth/register/*", "/auth/login", "/auth/validate","auth/test").permitAll();
        });

        // already registered as a bean
//        http.authenticationProvider(authenticationProvider());

        // login handled by auth controller
//        http.formLogin(r -> {
//            r.usernameParameter("username");
//            r.passwordParameter("password");
//            r.successHandler(authenticationSuccessHandlerImpl);
//            r.failureHandler(authenticationFailureHandlerImpl);
//        });


        http.exceptionHandling(c -> {
            c.accessDeniedHandler(accessDeniedHandlerImpl);
            c.authenticationEntryPoint(authenticationEntryPointImpl);
        });


        http.logout(c -> {
            c.permitAll()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(logoutSuccessHandlerImpl);
        });

        return http.build();
    }

    @Bean // put the return object into spring container, as a bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // You should only set trusted site here. e.g. http://localhost:4200 means only this site can access.
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","HEAD","OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //For JWT token based auth
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
