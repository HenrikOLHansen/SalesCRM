package com.example.application.data.repository;

import com.example.application.data.entity.CompletedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {
    List<CompletedTask> findAllByUsernameOrderByCompletionDateAsc(final String username);
}
