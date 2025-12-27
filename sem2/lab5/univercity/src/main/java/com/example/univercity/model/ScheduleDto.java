package com.example.univercity.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleDto {
    private Long id;
    private LocalDate date;        // Дата
    private LocalTime startTime;   // Время начала
    private LocalTime endTime;     // Время окончания
    private String subject;        // Предмет
    private String teacherName;    // Преподаватель
    private String groupName;      // Группа
    private String room;           // Аудитория
    private String dayOfWeek;      // День недели (опционально)

    public ScheduleDto() {}

    public ScheduleDto(
            Long id,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String subject,
            String teacherName,
            String groupName,
            String room
    ) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.teacherName = teacherName;
        this.groupName = groupName;
        this.room = room;
        this.dayOfWeek = date.getDayOfWeek().toString(); // Автоматически определяем день недели
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { 
        this.date = date;
        if (date != null) {
            this.dayOfWeek = date.getDayOfWeek().toString();
        }
    }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    // Метод для получения форматированного времени
    public String getFormattedTime() {
        if (startTime != null && endTime != null) {
            return startTime.toString() + " - " + endTime.toString();
        } else if (startTime != null) {
            return startTime.toString();
        }
        return "";
    }
}