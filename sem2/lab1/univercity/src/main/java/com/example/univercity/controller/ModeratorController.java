package com.example.univercity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/moderator")
public class ModeratorController {
    @GetMapping("/home")
    public String moderatorHome() {
        return "moderator"; // moderator.html в templates
    }
}
