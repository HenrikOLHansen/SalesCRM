package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Lead extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "customer_contact_id")
    @NotNull
    private Contact customerContact;

    @ManyToOne
    @JoinColumn(name = "additional_contact_id")
    private Contact additionalContact;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultant_id")
    private List<Consultant> consultants;

    @ManyToOne
    @JoinColumn(name = "consid_contact_id")
    private ConsidContact considContact;

    @Column(length = 2000)
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private LeadStatus leadStatus;

    public Contact getCustomerContact() { return customerContact; }

    public void setCustomerContact(Contact customerContact) {
        this.customerContact = customerContact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Consultant> getConsultants() { return consultants; }

    public void setConsultants(List<Consultant> consultants) { this.consultants = consultants; }

    public ConsidContact getConsidContact() { return considContact; }

    public void setConsidContact(ConsidContact considContact) { this.considContact = considContact; }

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

    public LeadStatus getLeadStatus() { return leadStatus; }

    public void setLeadStatus(LeadStatus leadStatus) { this.leadStatus = leadStatus; }

    public Contact getAdditionalContact() { return additionalContact; }

    public void setAdditionalContact(Contact additionalContact) { this.additionalContact = additionalContact; }
}
