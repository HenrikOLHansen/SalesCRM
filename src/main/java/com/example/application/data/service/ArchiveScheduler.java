package com.example.application.data.service;

import com.example.application.data.entity.Assignment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ArchiveScheduler {

    private final CrmService crmService;

    public ArchiveScheduler(CrmService crmService) {
        this.crmService = crmService;
    }

    @Scheduled(cron = "0 0 1 * * *") // Every day at 1 AM
    public void archiveAssignments() {
        System.out.println("I am now trying to find old Assignments...");
        LocalDate now = LocalDate.now();
        List<Assignment> oldAssignments = crmService.findAllAssignments().stream()
                .filter(assignment -> assignment.getEndDate().isBefore(now))
                .toList();

        for (Assignment assignment : oldAssignments) {
            crmService.archiveAssignment(assignment);
        }

        System.out.println("Found " + oldAssignments.size() + " old assignments which will be archived...");

    }

}
