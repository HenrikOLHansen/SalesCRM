package com.consid.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsidContact extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

}
