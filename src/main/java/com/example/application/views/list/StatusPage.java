package com.example.application.views.list;

import com.example.application.data.entity.Assignment;
import com.example.application.data.entity.Consultant;
import com.example.application.data.entity.Task;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.XAxis;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.ThemeBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.theme.Mode;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
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
    TaskList taskList;

    public StatusPage(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Status");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();

        SplitLayout topLayout = new SplitLayout(configureOccupancyChart(), configureTaskList());
        topLayout.setSizeFull();
        topLayout.setSplitterPosition(40);

        add(topLayout);

        add(new Hr());

        add(new H5("Assignments Ending Within A Month"));

        configureClosingAssignments();
        add(closingAssignments);

        updateContent();
    }

    private VerticalLayout configureTaskList() {
        H5 header = new H5("Current Tasks");

        Grid<Task> grid = new Grid<>(Task.class, false);
        grid.setSizeFull();
        grid.addColumn(task -> task.getContact().toString()).setHeader("Contact");
        grid.addColumn(Task::getDueDate).setHeader("Due Date");
        grid.addColumn(Task::getDescription).setHeader("Description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(crmService.findLastFiveTasks());

        return new VerticalLayout(header, grid);
    }

    private VerticalLayout configureOccupancyChart() {
        H5 header = new H5("Consultant Occupancy");

        List<Consultant> allConsultants = crmService.findAllConsultants();

        int totalNumberOfConsultants = allConsultants.size();

        int unassigned = (int) allConsultants.stream()
                .filter(c -> c.getAssignments().isEmpty())
                .count();
        int assignedConsultants = totalNumberOfConsultants - unassigned;

        Series<Integer> series = new Series<>(unassigned, assignedConsultants, totalNumberOfConsultants);
        XAxis xAxis = XAxisBuilder.get().withCategories("Available", "On Assignment", "Total").build();

        ApexCharts chart = ApexChartsBuilder.get().withChart(ChartBuilder.get()
                .withBackground("#233348")
                .withType(Type.BAR)
                .withHeight("200")
                .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(true)
                                .withDistributed(true)
                                .build())
                        .build())
                .withSeries(series)
                .withXaxis(xAxis)
                .withColors("#E91E63", "#00E396", "#2E93fA")
                .build();
        chart.setTheme(ThemeBuilder.get().withMode(Mode.DARK).build());

        return new VerticalLayout(header, chart);
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
