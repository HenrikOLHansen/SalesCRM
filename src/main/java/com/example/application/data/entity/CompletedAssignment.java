package com.example.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class CompletedAssignment extends AbstractEntity {

    @NotNull
    private String consultant;
    @NotNull
    private String customerContact;

    private String additionalContact;

    private String considContact;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = DESC_MAX_LENGTH)
    private String description;

    public CompletedAssignment() { super(); }

    public CompletedAssignment(Assignment assignment) {
        this.consultant = assignment.getConsultant().toString();
        this.customerContact = assignment.getCustomerContact().toString();
        this.additionalContact = assignment.getAdditionalContact() != null ? assignment.getAdditionalContact().toString() : "";
        this.considContact = assignment.getConsidContact().getFirstName();
        this.startDate = assignment.getStartDate();
        this.endDate = assignment.getEndDate();
        this.description = assignment.getDescription();
    }
}
