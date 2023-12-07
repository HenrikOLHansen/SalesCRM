package com.consid.application.views.list;

import com.consid.application.data.service.AssignmentService;
import com.consid.application.views.MainLayout;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.fill.builder.GradientBuilder;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.theme.Mode;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.DateCoordinate;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

@PermitAll
@PageTitle("Forecasts | Sales CRM")
@Route(value = "forecast", layout = MainLayout.class)
@Slf4j
public class ForecastPage extends VerticalLayout {

    private final AssignmentService assignmentService;

    public ForecastPage(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
        setAlignItems(Alignment.CENTER);

        add(new H5("Assignment Timeline"));

        add(createChart());
    }

    private VerticalLayout createChart() {
        var firstDayNextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());

        var coordinates = new ArrayList<DateCoordinate<String>>();
        var assignments = assignmentService.getAssignmentsFromDate(firstDayNextMonth);

        for(AssignmentService.ConsultantAssignments ca : assignments) {
            ca.orderedAssignments().forEach(oa -> coordinates.add(new DateCoordinate<>(ca.consultantName(), oa.start(), oa.end())));
        }

        Series<DateCoordinate<String>> series = new Series<>();

        DateCoordinate<String>[] data = new DateCoordinate[coordinates.size()];

        for(int i=0; i<coordinates.size(); i++) {
            data[i] = coordinates.get(i);
        }

        series.setData(data);

        return new VerticalLayout(Alignment.CENTER, createTimelineChart().withSeries(series).build());
    }

    private ApexChartsBuilder createTimelineChart() {
        double chartXMin = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        double chartXMax = LocalDateTime.now().plusMonths(6).toInstant(ZoneOffset.UTC).toEpochMilli();

        return ApexChartsBuilder.get().withChart(ChartBuilder.get()
                        .withBackground("#233348")
                        .withHeight("400")
                        .withType(Type.RANGEBAR)
                        .build())
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.RIGHT)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get().withHorizontal(true)
                                .build())
                        .build())
                .withLegend(LegendBuilder.get()
                        .withShow(false)
                        .build())
                .withTheme(ThemeBuilder.get().withMode(Mode.DARK).build())
                .withXaxis(XAxisBuilder.get()
                        .withType(XAxisType.DATETIME)
                        .withMin(chartXMin)
                        .withMax(chartXMax)
                        .build())
                //.withDataLabels(DataLabelsBuilder.get()
                //        .withEnabled(true)
                //        .withFormatter(ForecastPage::assignmentFormatter)
                //        .build())
                .withFill(FillBuilder.get()
                        .withType("gradient")
                        .withGradient(GradientBuilder.get()
                                .withShade("light")
                                .withType("horizontal")
                                .withShadeIntensity(0.50)
                                .build())
                        .build());
    }

    private static String assignmentFormatter() {
        return """
                function(val) {
                    const options = {
                      year: 'numeric',
                      month: 'short',
                      day: 'numeric',
                    };
                    
                    var a = new Date(val[0]).toLocaleDateString('en-US', options)
                    var b = new Date(val[1]).toLocaleDateString('en-US', options)
                    return a + ' -> ' + b
                  }
                """;
    }
}
