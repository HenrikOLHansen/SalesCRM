package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class ConsidContact extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

}
