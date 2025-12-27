package com.example.univercity;  // ← тот же пакет что и главный класс

import com.example.univercity.model.ChangeRequest;
import com.example.univercity.repository.jpa.ChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private ChangeRequestRepository repository;
    
    @GetMapping("/ping")
    public String ping() {
        return "API работает!";
    }
    
    @GetMapping("/all")
    public List<ChangeRequest> getAll() {
        return repository.findAll();
    }
}