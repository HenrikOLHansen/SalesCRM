package com.consid.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class CompletedTask extends AbstractTask {

    @Column(length = DESC_MAX_LENGTH)
    private String notes;

    private LocalDate completionDate;

    public CompletedTask() { super(); }

    public CompletedTask(Task task) {
        this.completionDate = LocalDate.now();

        setContact(task.getContact());
        setDescription(task.getDescription());
        setLink(task.getLink());
        setDueDate(task.getDueDate());
        setUsername(task.getUsername());
    }
}
