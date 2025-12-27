package com.example.univercity.model;

import java.time.LocalDateTime;

public class ChangeRequestDto {
    private Long id;
    private String teacherName;
    private String groupName;
    private String subject;
    private LocalDateTime currentDateTime;  // Используем LocalDateTime
    private LocalDateTime requestedDateTime; // Используем LocalDateTime
    private String reason;
    private String status;
    private String moderatorComment; // Добавляем комментарий модератора

    public ChangeRequestDto() {}

    public ChangeRequestDto(
            Long id,
            String teacherName,
            String groupName,
            String subject,
            LocalDateTime currentDateTime,
            LocalDateTime requestedDateTime,
            String reason,
            String status,
            String moderatorComment
    ) {
        this.id = id;
        this.teacherName = teacherName;
        this.groupName = groupName;
        this.subject = subject;
        this.currentDateTime = currentDateTime;
        this.requestedDateTime = requestedDateTime;
        this.reason = reason;
        this.status = status;
        this.moderatorComment = moderatorComment;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getModeratorComment() { return moderatorComment; }
    public void setModeratorComment(String moderatorComment) { this.moderatorComment = moderatorComment; }
}