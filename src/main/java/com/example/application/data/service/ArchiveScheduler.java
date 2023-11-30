package com.example.application.data.service;

import com.example.application.data.entity.Assignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArchiveScheduler {

    private final CrmService crmService;

    @Scheduled(cron = "0 0 1 * * *") // Every day at 1 AM
    public void archiveAssignments() {
        log.info("I am now trying to find old Assignments...");
        LocalDate now = LocalDate.now();
        List<Assignment> oldAssignments = crmService.findAllAssignments().stream()
                .filter(assignment -> assignment.getEndDate().isBefore(now))
                .toList();

        for (Assignment assignment : oldAssignments) {
            crmService.archiveAssignment(assignment);
        }

        log.info("Found " + oldAssignments.size() + " old assignments which will be archived...");

    }

}
