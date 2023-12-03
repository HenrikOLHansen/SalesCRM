package com.consid.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
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
}
