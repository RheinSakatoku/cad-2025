package com.example.univercity.controller;

import com.example.univercity.model.Schedule;
import com.example.univercity.model.ChangeRequest;
import com.example.univercity.service.ChangeRequestService;
import com.example.univercity.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {
    
    @Autowired
    private ChangeRequestService changeRequestService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @GetMapping("/home")
    public String moderatorHome(Model model, Authentication auth) {
        // Получаем pending запросы
        List<ChangeRequest> pendingRequests = changeRequestService.getPendingRequests();
        
        // Получаем всё расписание
        List<Schedule> allSchedules = scheduleService.getAllSchedules();
        
        // Передаем в модель
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("allSchedules", allSchedules);
        model.addAttribute("username", auth.getName());
        
        return "moderator";
    }
    
    @PostMapping("/approve/{requestId}")
    public String approveRequest(@PathVariable Long requestId, 
                                @RequestParam String comment) {
        changeRequestService.approveRequest(requestId, comment);
        return "redirect:/moderator/home?approved";
    }
    
    @PostMapping("/reject/{requestId}")
    public String rejectRequest(@PathVariable Long requestId,
                               @RequestParam String comment) {
        changeRequestService.rejectRequest(requestId, comment);
        return "redirect:/moderator/home?rejected";
    }
}