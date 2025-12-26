package com.example.univercity.controller;

import com.example.univercity.model.ChangeRequest;
import com.example.univercity.model.Schedule;
import com.example.univercity.model.User;
import com.example.univercity.repository.jpa.UserRepository;
import com.example.univercity.repository.jpa.ScheduleRepository;
import com.example.univercity.repository.jpa.ChangeRequestRepository;
import com.example.univercity.service.ChangeRequestService;
import com.example.univercity.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private ChangeRequestService changeRequestService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @GetMapping("/home")
    public String teacherHome(Model model, Authentication auth) {
        // Получаем преподавателя
        Optional<User> teacherOpt = userRepository.findByUsername(auth.getName());
        if (teacherOpt.isEmpty()) {
            return "redirect:/login?error";
        }
        User teacher = teacherOpt.get();
        
        // Получаем расписание преподавателя
        List<Schedule> schedules = scheduleRepository.findByTeacherName(teacher.getFullName());
        
        model.addAttribute("schedules", schedules);
        model.addAttribute("teacherName", teacher.getFullName());
        model.addAttribute("username", auth.getName());
        return "teacher";
    }
    
    @GetMapping("/request/{scheduleId}")
    public String showRequestForm(@PathVariable Long scheduleId, Model model, Authentication auth) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        Optional<User> teacherOpt = userRepository.findByUsername(auth.getName());
        
        if (scheduleOpt.isEmpty() || teacherOpt.isEmpty()) {
            return "redirect:/teacher/home?error";
        }
        
        model.addAttribute("schedule", scheduleOpt.get());
        model.addAttribute("teacherName", teacherOpt.get().getFullName());
        return "teacher-request-form";
    }
    
    @PostMapping("/request")
    public String submitRequest(
            @RequestParam Long scheduleId,
            @RequestParam String newDate,
            @RequestParam String newStartTime,
            @RequestParam String newEndTime,
            @RequestParam String reason,
            Authentication auth) {
        
        // Находим расписание
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        Optional<User> teacherOpt = userRepository.findByUsername(auth.getName());
        
        if (scheduleOpt.isEmpty() || teacherOpt.isEmpty()) {
            return "redirect:/teacher/home?error";
        }
        
        Schedule schedule = scheduleOpt.get();
        User teacher = teacherOpt.get();
        
        // Парсим дату и время из формы
        LocalDate parsedNewDate = LocalDate.parse(newDate);
        LocalTime parsedNewStartTime = LocalTime.parse(newStartTime);
        LocalTime parsedNewEndTime = LocalTime.parse(newEndTime);
        
        // Создаем запрос на изменение
        ChangeRequest request = new ChangeRequest();
        request.setScheduleId(scheduleId);
        request.setTeacherName(teacher.getFullName());
        request.setGroupName(schedule.getGroupName());
        request.setSubject(schedule.getSubject());
        
        // Текущее время пары
        LocalDateTime currentDateTime = LocalDateTime.of(schedule.getDate(), schedule.getStartTime());
        request.setCurrentDateTime(currentDateTime);
        
        // Запрашиваемое время в виде LocalDateTime (для совместимости)
        LocalDateTime requestedDateTime = LocalDateTime.of(parsedNewDate, parsedNewStartTime);
        request.setRequestedDateTime(requestedDateTime);
        
        //Без них approve не будет работать
        request.setNewDate(parsedNewDate);                    
        request.setNewStartTime(parsedNewStartTime);         
        request.setNewEndTime(parsedNewEndTime);              
        
        request.setReason(reason);
        request.setStatus("PENDING");
        request.setRequestedBy(auth.getName());
        // requestedAt уже установлен в конструкторе ChangeRequest
        
        changeRequestService.createRequest(request);
        
        return "redirect:/teacher/home?success";
    }
}