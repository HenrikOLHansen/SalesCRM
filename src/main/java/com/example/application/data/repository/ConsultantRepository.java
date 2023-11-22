package com.example.application.data.repository;

import com.example.application.data.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    @Query("select c from Consultant c " +
            "join c.skills skill " +
            "where lower(skill.name) like lower(concat('%', :searchTerm, '%'))")
    List<Consultant> search(@Param("searchTerm") String searchTerm);
}
