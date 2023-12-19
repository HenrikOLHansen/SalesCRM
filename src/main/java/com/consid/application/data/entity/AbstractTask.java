package com.consid.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractTask extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "task_contact_id")
    private Contact contact;

    private String link;

    @Column(length = DESC_MAX_LENGTH)
    private String description;

    private LocalDate dueDate;

     private String username;
}
