package com.example.univercity.repository.jpa;

import com.example.univercity.model.ChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long> {
    List<ChangeRequest> findByStatus(String status);
    List<ChangeRequest> findByRequestedBy(String requestedBy);
    List<ChangeRequest> findByStatusOrderByRequestedAtDesc(String status);
}