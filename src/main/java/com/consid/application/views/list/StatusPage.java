package com.consid.application.views.list;

import com.consid.application.data.entity.Assignment;
import com.consid.application.data.entity.Task;
import com.consid.application.data.service.CrmService;
import com.consid.application.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@PermitAll
@PageTitle("Status | Sales CRM")
@Route(value = "", layout = MainLayout.class)
public class StatusPage extends VerticalLayout {

    private final CrmService crmService;
    Grid<Assignment> closingAssignments = new Grid<>(Assignment.class, false);
    Grid<Task> grid;

    public StatusPage(final CrmService crmService) {
        this.crmService = crmService;

        setSizeFull();

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(new H5("Tasks Due Today"));

        configureTaskList();
        add(grid);

        add(new Hr());

        add(new H5("Assignments Ending Within A Month"));

        configureClosingAssignments();
        add(closingAssignments);

        updateContent();
    }

    private void configureTaskList() {
        grid = new Grid<>(Task.class, false);
        grid.setSizeFull();
        grid.addColumn(task -> task.getContact().toString()).setHeader("Contact");
        grid.addColumn(Task::getDueDate).setHeader("Due Date");
        grid.addColumn(Task::getDescription).setHeader("Description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(crmService.findAllCurrentTasks());
    }

    private void configureClosingAssignments() {
        LocalDate now = LocalDate.now();
        closingAssignments.addClassName("closing-assignments-grid");
        closingAssignments.setSizeFull();
        closingAssignments.addColumn(assignment -> assignment.getConsultant().toString()).setHeader("Consultant");
        closingAssignments.addColumn(assignment -> assignment.getCustomerContact().toString()).setHeader("Contact: Customer");
        closingAssignments.addComponentColumn(assignment -> assignmentDaysStatus(now, assignment)).setHeader("Days Until End");
        closingAssignments.addColumn(Assignment::getStartDate).setHeader("Start Date");
        closingAssignments.addColumn(Assignment::getEndDate).setHeader("End Date");
    }

    private Span assignmentDaysStatus(LocalDate now, Assignment assignment) {
        int daysUntilAssignmentEnds = daysUntilAssignmentEnds(now, assignment);
        String theme = daysUntilAssignmentEnds > 15 ? "badge" : "badge error";
        String status = daysUntilAssignmentEnds + " days";
        Span span = new Span(status);
        span.getElement().getThemeList().add(theme);
        return span;
    }

    private void updateContent() {
        LocalDate now = LocalDate.now();
        List<Assignment> endingAssignments = crmService.findAllAssignments().stream()
                .filter(assignment -> daysUntilAssignmentEnds(now, assignment) <= 31)
                .toList();

        closingAssignments.setItems(endingAssignments);
    }

    private int daysUntilAssignmentEnds(LocalDate now, Assignment assignment) {
        return (int) ChronoUnit.DAYS.between(now, assignment.getEndDate());
    }

}
