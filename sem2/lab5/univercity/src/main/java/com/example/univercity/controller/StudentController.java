package com.example.univercity.controller;

import com.example.univercity.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @GetMapping("/home")
    public String studentHome(Model model, Authentication auth) {
        model.addAttribute("schedules", scheduleService.getScheduleForStudent(auth.getName()));
        model.addAttribute("username", auth.getName());
        return "student";
    }
}