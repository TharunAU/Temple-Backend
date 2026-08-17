package com.thittaiAmman.thittai_backend.configuration;

import com.thittaiAmman.thittai_backend.Filter.JWTFilter;
import com.thittaiAmman.thittai_backend.service.MyUsersDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        logger.info("Security Config Loaded");
        return http.csrf(customizer->customizer.disable())
                .authorizeHttpRequests(request->request
                        .requestMatchers("/login","/signup","/getFestivals/**","/test").permitAll()
                        //Gallery - public read, admin write/delete
                        .requestMatchers(HttpMethod.GET,"/gallery/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/addToGallery").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/gallery/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/gallery/image/**").hasRole("ADMIN")
                        //Festivals - public read, admin create/update/delete
                        .requestMatchers(HttpMethod.GET,"/getFestivals/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/addFestivals").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/updateFest/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/deleteFestival/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
