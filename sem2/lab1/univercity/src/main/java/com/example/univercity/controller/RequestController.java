package com.example.univercity.controller;

import com.example.univercity.model.ChangeRequest;
import com.example.univercity.repository.jpa.ChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    
    @Autowired
    private ChangeRequestRepository requestRepository;
    
    @GetMapping("/")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<ChangeRequest> getAllRequests() {
        return requestRepository.findAll();
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<ChangeRequest> getPendingRequests() {
        return requestRepository.findByStatus("PENDING");
    }
    
    @GetMapping("/teacher/{username}")
    @PreAuthorize("#username == authentication.name or hasRole('MODERATOR')")
    public List<ChangeRequest> getTeacherRequests(@PathVariable String username) {
        return requestRepository.findByRequestedBy(username);
    }
    
    @PostMapping("/")
    @PreAuthorize("hasRole('TEACHER')")
    public ChangeRequest createRequest(@RequestBody ChangeRequest request) {
        request.setStatus("PENDING");
        return requestRepository.save(request);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('MODERATOR')")
    public ChangeRequest updateStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     @RequestParam(required = false) String comment) {
        ChangeRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus(status);
        if (comment != null) {
            request.setModeratorComment(comment);
        }
        return requestRepository.save(request);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String deleteRequest(@PathVariable Long id) {
        requestRepository.deleteById(id);
        return "Request deleted";
    }
}