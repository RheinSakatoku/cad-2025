package com.example.univercity.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "change_requests")
public class ChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;
    
    @Column(name = "teacher_name", nullable = false)
    private String teacherName;
    
    @Column(name = "group_name", nullable = false)
    private String groupName;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(name = "current_date_time", nullable = false)
    private LocalDateTime currentDateTime;
    
    @Column(name = "requested_date_time", nullable = true)
    private LocalDateTime requestedDateTime;
    
    @Column(nullable = true)
    private String reason;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "requested_by", nullable = false)
    private String requestedBy;
    
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;
    
   @Column(name = "moderator_comment", nullable = true) 
    private String moderatorComment;

    @Column(name = "new_date", nullable = true)
    private LocalDate newDate;
    
    @Column(name = "new_start_time", nullable = true) 
    private LocalTime newStartTime;
    
    @Column(name = "new_end_time", nullable = true) 
    private LocalTime newEndTime;
    
    public LocalDate getNewDate() { return newDate; }
    public void setNewDate(LocalDate newDate) { this.newDate = newDate; }
    
    public LocalTime getNewStartTime() { return newStartTime; }
    public void setNewStartTime(LocalTime newStartTime) { this.newStartTime = newStartTime; }
    
    public LocalTime getNewEndTime() { return newEndTime; }
    public void setNewEndTime(LocalTime newEndTime) { this.newEndTime = newEndTime; }

    public ChangeRequest() {
        this.status = "PENDING";
        this.requestedAt = LocalDateTime.now();
    }
    
    public ChangeRequest(Long scheduleId, String teacherName, String groupName, 
                        String subject, LocalDateTime currentDateTime,
                        LocalDateTime requestedDateTime, String reason) {
        this();
        this.scheduleId = scheduleId;
        this.teacherName = teacherName;
        this.groupName = groupName;
        this.subject = subject;
        this.currentDateTime = currentDateTime;
        this.requestedDateTime = requestedDateTime;
        this.reason = reason;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public LocalDateTime getCurrentDateTime() { return currentDateTime; }
    public void setCurrentDateTime(LocalDateTime currentDateTime) { this.currentDateTime = currentDateTime; }
    
    public LocalDateTime getRequestedDateTime() { return requestedDateTime; }
    public void setRequestedDateTime(LocalDateTime requestedDateTime) { this.requestedDateTime = requestedDateTime; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    
    public String getModeratorComment() { return moderatorComment; }
    public void setModeratorComment(String moderatorComment) { this.moderatorComment = moderatorComment; }
}