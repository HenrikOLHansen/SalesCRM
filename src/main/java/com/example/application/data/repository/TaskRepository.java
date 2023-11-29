package com.example.application.data.repository;

import com.example.application.data.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUsernameOrderByDueDateAsc(String username);
}
