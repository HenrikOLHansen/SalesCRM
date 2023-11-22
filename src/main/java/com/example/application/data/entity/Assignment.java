package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Assignment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    @NotNull
    private Consultant consultant;

    @ManyToOne
    @JoinColumn(name = "customer_contact_id")
    @NotNull
    private Contact customerContact;

    @ManyToOne
    @JoinColumn(name = "additional_contact_id")
    private Contact additionalContact;

    @ManyToOne
    @JoinColumn(name = "consid_contact_id")
    private ConsidContact considContact;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = DESC_MAX_LENGTH)
    private String description;

    public Assignment() {
        super();
    }

    public Assignment(Lead lead, Consultant consultant) {
        super();
        this.consultant = consultant;
        this.customerContact = lead.getCustomerContact();
        this.additionalContact = lead.getAdditionalContact();
        this.considContact = lead.getConsidContact();
        this.startDate = lead.getStartDate();
        this.endDate = lead.getEndDate();
        this.description = lead.getDescription();
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

    public Contact getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(Contact customerContact) {
        this.customerContact = customerContact;
    }

    public ConsidContact getConsidContact() {
        return considContact;
    }

    public void setConsidContact(ConsidContact considContact) {
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

    public Contact getAdditionalContact() { return additionalContact; }

    public void setAdditionalContact(Contact additionalContact) { this.additionalContact = additionalContact; }
}
