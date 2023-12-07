package com.consid.application.data.service;

import com.consid.application.data.entity.Assignment;
import com.consid.application.data.entity.Consultant;
import com.consid.application.data.repository.ConsultantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class AssignmentService {

    private final ConsultantRepository consultantRepository;

    public AssignmentService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    public List<ConsultantAssignments> getAssignmentsFromDate(LocalDate fromDate) {
        var allConsultants = consultantRepository.findAll();
        var allAssignments = new ArrayList<ConsultantAssignments>();
        allConsultants.forEach(consultant -> allAssignments.add(buildConsultantAssignments(consultant, fromDate)));
        return allAssignments;
    }

    private ConsultantAssignments buildConsultantAssignments(Consultant consultant, LocalDate fromDate) {
        var assignments = new ConsultantAssignments(consultant.getFirstName(), new LinkedList<>());
        consultant.getAssignments().stream()
                .sorted(Comparator.comparing(Assignment::getEndDate))
                .forEach(assignment -> assignments.orderedAssignments
                        .add(new AssignmentRange(assignment.getStartDate(), assignment.getEndDate())));

        return assignments;
    }

    public record ConsultantAssignments(String consultantName, LinkedList<AssignmentRange> orderedAssignments) {}

    public record AssignmentRange(LocalDate start, LocalDate end) {}
}
