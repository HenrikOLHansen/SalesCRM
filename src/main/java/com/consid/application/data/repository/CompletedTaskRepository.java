package com.consid.application.data.repository;

import com.consid.application.data.entity.CompletedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {
    List<CompletedTask> findAllByUsernameOrderByCompletionDateAsc(final String username);
}
