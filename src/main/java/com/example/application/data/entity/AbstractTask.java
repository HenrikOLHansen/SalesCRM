package com.example.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
public abstract class AbstractTask extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "task_contact_id")
    private Contact contact;

    private String link;

    @Column(length = DESC_MAX_LENGTH)
    private String description;

    private LocalDate dueDate;

     private String username;

    public AbstractTask() { super(); }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
