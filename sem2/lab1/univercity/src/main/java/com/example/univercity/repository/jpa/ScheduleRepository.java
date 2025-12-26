package com.example.univercity.repository.jpa;

import com.example.univercity.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByGroupName(String groupName);
    List<Schedule> findByTeacherName(String teacherName);
    List<Schedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Schedule> findByGroupNameAndDateBetween(String groupName, LocalDate startDate, LocalDate endDate);
}