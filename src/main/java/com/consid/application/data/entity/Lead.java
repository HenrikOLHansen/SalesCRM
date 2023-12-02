package com.consid.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
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
}
