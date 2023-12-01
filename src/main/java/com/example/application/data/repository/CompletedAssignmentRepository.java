package com.example.application.data.repository;

import com.example.application.data.entity.CompletedAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedAssignmentRepository extends JpaRepository<CompletedAssignment, Long> {

    List<CompletedAssignment> findAllByOrderByEndDateDesc();

}
