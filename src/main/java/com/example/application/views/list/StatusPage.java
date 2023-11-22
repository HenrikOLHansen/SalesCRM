package com.example.application.views.list;

import com.example.application.data.entity.Assignment;
import com.example.application.data.entity.Consultant;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
public class StatusPage extends VerticalLayout {

    private final CrmService crmService;
    Grid<Assignment> closingAssignments = new Grid<>(Assignment.class, false);

    public StatusPage(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Status");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        H5 occupancyHeader = new H5("Consultant Occupancy");
        add(occupancyHeader);

        setSizeFull();

        ApexCharts chart = configureOccupancyChart();
        add(chart);

        add(new Hr());

        add(new H5("Assignments Ending Within A Month"));

        configureClosingAssignments();
        add(closingAssignments);

        updateContent();
    }

    private ApexCharts configureOccupancyChart() {
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

        return chart;
    }

    private void configureClosingAssignments() {
        LocalDate now = LocalDate.now();
        closingAssignments.addClassName("closing-assignments-grid");
        closingAssignments.setSizeFull();
        closingAssignments.addColumn(assignment -> assignment.getConsultant().toString()).setHeader("Consultant");
        closingAssignments.addColumn(assignment -> assignment.getCustomerContact().toString()).setHeader("Contact: Customer");
        closingAssignments.addColumn(assignment -> daysUntilAssignmentEnds(now, assignment)).setHeader("Days Until End");
        closingAssignments.addColumn(Assignment::getStartDate).setHeader("Start Date");
        closingAssignments.addColumn(Assignment::getEndDate).setHeader("End Date");
    }

    private void updateContent() {
        LocalDate now = LocalDate.now();
        List<Assignment> endingAssignments = crmService.findAllAssignments().stream()
                .filter(assignment -> daysUntilAssignmentEnds(now, assignment) <= 31)
                .toList();

        closingAssignments.setItems(endingAssignments);

        // TODO reset series content on chart

    }

    private int daysUntilAssignmentEnds(LocalDate now, Assignment assignment) {
        return (int) ChronoUnit.DAYS.between(now, assignment.getEndDate());
    }

}
