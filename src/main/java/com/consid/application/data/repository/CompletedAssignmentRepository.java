package com.consid.application.data.repository;

import com.consid.application.data.entity.CompletedAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedAssignmentRepository extends JpaRepository<CompletedAssignment, Long> {

    List<CompletedAssignment> findAllByOrderByEndDateDesc();

}
