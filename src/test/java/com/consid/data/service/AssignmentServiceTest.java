package com.consid.data.service;

import com.consid.application.Application;
import com.consid.application.data.service.AssignmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = Application.class)
public class AssignmentServiceTest {

    @Autowired
    private AssignmentService assignmentService;

    @Test
    void testUtilization() {
        var startDate = LocalDate.of(2024, 1, 1);
        var endDate = startDate.plusMonths(6);
        var rangeMap = assignmentService.createDateRangeMap(startDate, endDate);

        assertEquals(182, rangeMap.size());
        assertEquals(40.0D, rangeMap.get(startDate));
        assertEquals(60.0D, rangeMap.get(LocalDate.of(2024, 1, 12)));
        assertEquals(100.0D, rangeMap.get(LocalDate.of(2024, 3, 6)));
    }
}
