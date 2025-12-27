package com.example.univercity.service;

import com.example.univercity.model.Schedule;
import com.example.univercity.model.User;
import com.example.univercity.repository.jpa.ScheduleRepository;
import com.example.univercity.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Schedule> getScheduleForStudent(String username) {
        User student = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return scheduleRepository.findByGroupName(student.getGroupName());
    }
    
    public List<Schedule> getScheduleForTeacher(String username) {
        User teacher = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return scheduleRepository.findByTeacherName(teacher.getFullName());
    }
    
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}