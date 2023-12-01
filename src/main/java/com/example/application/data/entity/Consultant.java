package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Consultant extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Skill> skills;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "consultant")
    private List<Assignment> assignments;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getShortSkillList() {
        return skills.stream()
                .map(Skill::getName)
                .limit(4)
                .collect(Collectors.joining(", "));
    }
}
