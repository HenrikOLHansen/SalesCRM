package com.consid.application.data.service;

import com.consid.application.data.entity.Assignment;
import com.consid.application.data.entity.Consultant;
import com.consid.application.data.repository.ConsultantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class AssignmentService {

    private final ConsultantRepository consultantRepository;

    public AssignmentService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    public List<ConsultantAssignments> getAssignmentsFromDate() {
        var allConsultants = consultantRepository.findAll();
        var allAssignments = new ArrayList<ConsultantAssignments>();
        allConsultants.forEach(consultant -> allAssignments.add(buildConsultantAssignments(consultant)));
        return allAssignments;
    }

    private ConsultantAssignments buildConsultantAssignments(Consultant consultant) {
        var assignments = new ConsultantAssignments(consultant.getFirstName(), new LinkedList<>());
        consultant.getAssignments().stream()
                .sorted(Comparator.comparing(Assignment::getEndDate))
                .forEach(assignment -> assignments.orderedAssignments
                        .add(new AssignmentRange(assignment.getStartDate(), assignment.getEndDate())));

        return assignments;
    }

    public LinkedHashMap<LocalDate, Double> createDateRangeMap(LocalDate startDate, LocalDate endDate) {
        // Prepare a map containing all days in the specified range
        // with a percentage of the consultant utilization per day
        var rangeMap = new LinkedHashMap<LocalDate, Double>();
        startDate.datesUntil(endDate).forEach(day -> rangeMap.put(day, 0.0));

        // Iterate through all consultant assignments and add days they are assigned
        var consultantAssignments = getAssignmentsFromDate();
        for(ConsultantAssignments assignments : consultantAssignments) {
            for(LocalDate day : rangeMap.keySet()) {
                if(assignments.isDayInRange(day)) {
                    rangeMap.merge(day, 1.0, Double::sum);
                }
            }
        }

        // Calculate percentage per day
        double totalConsultants = consultantAssignments.size();

        rangeMap.forEach((key, value) -> {
            var percentage = 100/(totalConsultants/value);
            rangeMap.replace(key, percentage);
        });

        return rangeMap;
    }

    public record ConsultantAssignments(String consultantName, LinkedList<AssignmentRange> orderedAssignments) {
        public boolean isDayInRange(LocalDate day) {
            for(AssignmentRange range : orderedAssignments) {
                if(range.isDayInRange(day)) {
                    return true;
                }
            }
            return false;
        }

    }

    public record AssignmentRange(LocalDate start, LocalDate end) {
        public boolean isDayInRange(LocalDate day) {
            return day.isAfter(start) && day.isBefore(end);
        }
    }


}
