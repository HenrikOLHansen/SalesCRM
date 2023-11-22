package com.example.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
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

    public String getConsultant() { return consultant; }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getConsidContact() {
        return considContact;
    }

    public void setConsidContact(String considContact) {
        this.considContact = considContact;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalContact() { return additionalContact; }

    public void setAdditionalContact(String additionalContact) { this.additionalContact = additionalContact; }
}
