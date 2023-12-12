package com.consid.application.views.list;

import com.consid.application.data.service.AssignmentService;
import com.consid.application.views.MainLayout;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.fill.builder.GradientBuilder;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.series.SeriesType;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.theme.Mode;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.config.yaxis.builder.LabelsBuilder;
import com.github.appreciated.apexcharts.config.yaxis.labels.builder.StyleBuilder;
import com.github.appreciated.apexcharts.helper.Coordinate;
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
import java.util.List;

@PermitAll
@PageTitle("Forecasts | Sales CRM")
@Route(value = "forecast", layout = MainLayout.class)
@Slf4j
public class ForecastPage extends VerticalLayout {

    public static final com.github.appreciated.apexcharts.config.yaxis.labels.Style Y_AXIS_LABEL_STYLE = StyleBuilder.get()
            .withFontSize("16px")
            .build();
    public static final com.github.appreciated.apexcharts.config.xaxis.labels.Style X_AXIS_LABEL_STYLE = com.github.appreciated.apexcharts.config.xaxis.labels.builder.StyleBuilder.get()
            .withFontSize("16px")
            .build();
    private final AssignmentService assignmentService;

    public ForecastPage(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
        setAlignItems(Alignment.CENTER);

        add(new H5("Utilization Percentage"));

        add(getUtilizationChart());

        add(new H5("Assignment Timeline"));

        add(getTimelineChart());
    }

    private VerticalLayout getTimelineChart() {
        var firstDayNextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()); // TODO remove

        var coordinates = new ArrayList<DateCoordinate<String>>();
        var assignments = assignmentService.getAssignmentsFromDate();

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

    private VerticalLayout getUtilizationChart() {

        var rangeData = assignmentService.createDateRangeMap(LocalDate.now(), LocalDate.now().plusMonths(6));

        Coordinate[] data = new Coordinate[rangeData.size()];

        int i = 0;
        for(LocalDate day : rangeData.keySet()) {
            data[i] = new Coordinate(LocalDateTime.from(day.atStartOfDay()).toInstant(ZoneOffset.UTC).toEpochMilli(), rangeData.get(day));
            i++;
        }

        Series series = new Series<>();
        series.setType(SeriesType.AREA);
        series.setData(data);

        return new VerticalLayout(Alignment.CENTER, createUtilizationChart().withSeries(series).build());
    }

    private ApexChartsBuilder createTimelineChart() {
        double chartXMin = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        double chartXMax = LocalDateTime.now().plusMonths(6).toInstant(ZoneOffset.UTC).toEpochMilli();

        return ApexChartsBuilder.get().withChart(ChartBuilder.get()
                        .withFontFamily("--lumo-font-family")
                        .withBackground("#233348")
                        .withHeight("250")
                        .withType(Type.RANGEBAR)
                        .build())
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.RIGHT)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(true)
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
                        .withLabels(com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder.get()
                                .withStyle(X_AXIS_LABEL_STYLE)
                                .build())
                        .build())
                .withYaxis(YAxisBuilder.get()
                        .withLabels(LabelsBuilder.get()
                                .withStyle(Y_AXIS_LABEL_STYLE)
                                .build())
                        .build())
                .withFill(FillBuilder.get()
                        .withType("gradient")
                        .withColors(List.of("#E91E63"))
                        .withGradient(GradientBuilder.get()
                                .withShade("light")
                                .withType("horizontal")
                                .withShadeIntensity(0.50)
                                .build())
                .build());
    }

    private ApexChartsBuilder createUtilizationChart() {
        return ApexChartsBuilder.get().withChart(ChartBuilder.get()
                    .withFontFamily("--lumo-font-family")
                    .withBackground("#233348")
                    .withHeight("200")
                    .withType(Type.AREA)
                    .withZoom(ZoomBuilder.get()
                            .withEnabled(false)
                            .build())
                    .withOffsetX(10.0D)
                    .build())
                .withTheme(ThemeBuilder.get().withMode(Mode.DARK).build())
                .withColors("#66DA26")
                .withStroke(StrokeBuilder.get().withCurve(Curve.SMOOTH).build())
                .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
                .withYaxis(YAxisBuilder.get()
                        .withLabels(LabelsBuilder.get()
                                .withStyle(Y_AXIS_LABEL_STYLE)
                                .build())
                        .withMin(0.0)
                        .withMax(100.0)
                        .build())
                .withXaxis(XAxisBuilder.get()
                        .withLabels(com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder.get()
                                .withStyle(X_AXIS_LABEL_STYLE)
                                .build())
                        .withType(XAxisType.DATETIME)
                        .build());
    }
}
