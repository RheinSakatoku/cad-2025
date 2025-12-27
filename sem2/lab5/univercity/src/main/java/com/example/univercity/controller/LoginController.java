package com.example.univercity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        System.out.println("DEBUG: Rendering login page");
        return "login";
    }

    @GetMapping("/default")
    public String redirectAfterLogin(Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            return "redirect:/student/home";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
            return "redirect:/teacher/home";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR"))) {
            return "redirect:/moderator/home";
        }
        return "redirect:/login";
    }
}
