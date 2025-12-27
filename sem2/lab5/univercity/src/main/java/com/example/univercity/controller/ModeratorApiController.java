package com.example.univercity.controller;

import com.example.univercity.model.ChangeRequest;
import com.example.univercity.model.Schedule;
import com.example.univercity.repository.jpa.ChangeRequestRepository;
import com.example.univercity.repository.jpa.ScheduleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderator")
@PreAuthorize("hasRole('MODERATOR')")
public class ModeratorApiController {

    private final ChangeRequestRepository changeRequestRepository;
    private final ScheduleRepository scheduleRepository;

    public ModeratorApiController(ChangeRequestRepository changeRequestRepository,
                                  ScheduleRepository scheduleRepository) {
        this.changeRequestRepository = changeRequestRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/all-requests")
    public List<ChangeRequest> getAllRequests() {
        return changeRequestRepository.findAll();
    }

    @GetMapping("/all-schedules")
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @PutMapping("/requests/{id}/approve")
    public ChangeRequest approve(@PathVariable Long id,
                                 @RequestParam String comment) {
        ChangeRequest request = changeRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");
        request.setModeratorComment(comment);
        return changeRequestRepository.save(request);
    }

    @PutMapping("/requests/{id}/reject")
    public ChangeRequest reject(@PathVariable Long id,
                                @RequestParam String comment) {
        ChangeRequest request = changeRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        request.setModeratorComment(comment);
        return changeRequestRepository.save(request);
    }
}