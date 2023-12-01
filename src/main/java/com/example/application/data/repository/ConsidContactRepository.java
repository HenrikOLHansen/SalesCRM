package com.example.application.data.repository;

import com.example.application.data.entity.ConsidContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsidContactRepository extends JpaRepository<ConsidContact, Long> {
}
