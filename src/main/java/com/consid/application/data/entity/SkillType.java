package com.consid.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@RequiredArgsConstructor
public class SkillType extends AbstractEntity {

    @NotEmpty
    private String type;
}
