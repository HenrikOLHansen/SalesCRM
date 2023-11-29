package com.example.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}
