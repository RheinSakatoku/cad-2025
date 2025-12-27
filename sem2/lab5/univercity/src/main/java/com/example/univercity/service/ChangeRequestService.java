package com.example.univercity.service;

import com.example.univercity.model.ChangeRequest;
import com.example.univercity.model.Schedule;
import com.example.univercity.repository.jpa.ChangeRequestRepository;
import com.example.univercity.repository.jpa.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeRequestService {
    @Autowired
    private ChangeRequestRepository requestRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    public ChangeRequest createRequest(ChangeRequest request) {
        request.setStatus("PENDING");
        return requestRepository.save(request);
    }
    
    public List<ChangeRequest> getPendingRequests() {
        return requestRepository.findByStatus("PENDING");
    }
    
    public ChangeRequest approveRequest(Long requestId, String moderatorComment) {
        ChangeRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        // Находим расписание
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        // Меняем время в расписании
        schedule.setDate(request.getNewDate());
        schedule.setStartTime(request.getNewStartTime());
        schedule.setEndTime(request.getNewEndTime());
        scheduleRepository.save(schedule);
        
        // Обновляем статус запроса
        request.setStatus("APPROVED");
        request.setModeratorComment(moderatorComment);
        return requestRepository.save(request);
    }
    
    public ChangeRequest rejectRequest(Long requestId, String moderatorComment) {
        ChangeRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus("REJECTED");
        request.setModeratorComment(moderatorComment);
        return requestRepository.save(request);
    }
}