package com.consid.application.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

// user is a reserved keyword
@Table(name = "user_table")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class User extends AbstractEntity {

        private String firstName;

        private String lastName;

        private String email;

        private String password;

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private List<Role> roles;
}
