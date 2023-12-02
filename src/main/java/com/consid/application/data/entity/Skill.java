package com.consid.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.ToString;

@Entity
@ToString
public class Skill extends AbstractEntity {

    @NotEmpty
    private String name;
    // TODO Type type

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}