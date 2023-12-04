package com.consid.application.data.repository;

import com.consid.application.data.entity.ConsidContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsidContactRepository extends JpaRepository<ConsidContact, Long> {
}
