package com.example.application.data.repository;

import com.example.application.data.entity.CompletedTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {
    List<CompletedTask> findAllByUsernameOrderByCompletionDateAsc(String username);
}
