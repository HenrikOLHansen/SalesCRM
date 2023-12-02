package com.consid.application.views.list;

import com.consid.application.data.entity.CompletedAssignment;
import com.consid.application.data.service.CrmService;
import com.consid.application.views.MainLayout;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Stream;

@PermitAll
@PageTitle("Completed Assignments | Sales CRM")
@Route(value = "completedAssignments", layout = MainLayout.class)
public class CompletedAssignmentList extends VerticalLayout {

    Grid<CompletedAssignment> grid = new Grid<>(CompletedAssignment.class, false);

    private final CrmService crmService;

    public CompletedAssignmentList(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Assignments");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(grid);

        configureGrid();
        updateGridContent();
    }

    private void configureGrid() {
        grid.addClassName("completed-assignment-grid");
        grid.setSizeFull();
        grid.addColumn(CompletedAssignment::getConsultant).setHeader("Consultant");
        grid.addColumn(CompletedAssignment::getCustomerContact).setHeader("Contact/Company");
        grid.addColumn(CompletedAssignment::getStartDate).setHeader("Start Date");
        grid.addColumn(CompletedAssignment::getEndDate).setHeader("End Date");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItemDetailsRenderer(createAssignmentRenderer());
    }

    private void updateGridContent() { grid.setItems(crmService.findAllCompletedAssignments()); }

    private static ComponentRenderer<AssignmentDetailsFormLayout, CompletedAssignment> createAssignmentRenderer() {
        return new ComponentRenderer<>(AssignmentDetailsFormLayout::new, AssignmentDetailsFormLayout::setAssignment);
    }

    private static class AssignmentDetailsFormLayout extends FormLayout {
        private final TextField consultant = new TextField("Consultant");
        private final TextField customerContact = new TextField("Customer Contact");
        private final TextField additionalContact = new TextField("Additional Contact");
        private final TextField considContact = new TextField("Consid Contact");
        private final DatePicker startDate = new DatePicker("Start Date");
        private final DatePicker endDate = new DatePicker("End Date");
        private final TextArea description = new TextArea("Description");

        public AssignmentDetailsFormLayout() {
            setColspan(description, 2);

            Stream.<AbstractSinglePropertyField>of(consultant, customerContact, additionalContact, considContact, startDate, endDate, description)
                    .forEach(field -> {
                        field.setReadOnly(true);
                        add(field);
                    });

            setResponsiveSteps(new ResponsiveStep("0", 2));
        }

        public void setAssignment(CompletedAssignment assignment) {
            consultant.setValue(assignment.getConsultant());
            customerContact.setValue(assignment.getCustomerContact());
            additionalContact.setValue(assignment.getAdditionalContact());
            considContact.setValue(assignment.getConsidContact());
            startDate.setValue(assignment.getStartDate());
            endDate.setValue(assignment.getEndDate());
            description.setValue(assignment.getDescription());
        }

    }
}
