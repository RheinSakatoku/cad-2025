package com.example.univercity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsersConfig {

    private final PasswordEncoder passwordEncoder;

    public UsersConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails student = User.builder()
            .username("student")
            .password(passwordEncoder.encode("123"))
            .roles("STUDENT")
            .build();

        UserDetails teacher = User.builder()
            .username("teacher")
            .password(passwordEncoder.encode("123"))
            .roles("TEACHER")
            .build();

        UserDetails moderator = User.builder()
            .username("moderator")
            .password(passwordEncoder.encode("123"))
            .roles("MODERATOR")
            .build();

        return new InMemoryUserDetailsManager(student, teacher, moderator);
    }
}
