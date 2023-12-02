package com.consid.application.data.repository;

import com.consid.application.data.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUsernameOrderByDueDateAsc(final String username);

    List<Task> findAllByUsernameAndDueDate(final String username, final LocalDate dueDate);
}
