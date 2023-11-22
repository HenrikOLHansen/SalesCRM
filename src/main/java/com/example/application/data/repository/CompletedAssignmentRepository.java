package com.example.application.data.repository;

import com.example.application.data.entity.CompletedAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletedAssignmentRepository extends JpaRepository<CompletedAssignment, Long> {

    public List<CompletedAssignment> findAllByOrderByEndDateDesc();

}
