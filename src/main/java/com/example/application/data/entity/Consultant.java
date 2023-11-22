package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Consultant extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Skill> skills;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "consultant")
    private List<Assignment> assignments;

    public Consultant() {
        skills = new ArrayList<>();
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) { this.skills.add(skill); }

    public List<Assignment> getAssignments() { return assignments; }

    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

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
