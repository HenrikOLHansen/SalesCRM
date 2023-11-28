package com.example.application.data.repository;

import com.example.application.data.entity.CompletedTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {
}
