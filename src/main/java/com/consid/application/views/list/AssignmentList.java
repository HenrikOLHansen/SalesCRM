package com.consid.application.views.list;

import com.consid.application.data.entity.Assignment;
import com.consid.application.views.MainLayout;
import com.consid.application.views.form.AssignmentForm;
import com.consid.application.data.service.CrmService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Assignments | Sales CRM")
@Route(value = "assignments", layout = MainLayout.class)
public class AssignmentList extends VerticalLayout {

    Grid<Assignment> grid = new Grid<>(Assignment.class, false);
    AssignmentForm assignmentForm;
    Dialog assignmentDialog = new Dialog();

    private final CrmService crmService;

    public AssignmentList(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Assignments");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(createToolbar(), grid);

        setupDialog();

        configureGrid();
        updateGridContent();
    }

    private Component createToolbar() {
        Button createNewAssignmentButton = new Button("New Assignment");
        createNewAssignmentButton.addClickListener(this::createNewAssignment);
        return new HorizontalLayout(createNewAssignmentButton);
    }

    private void createNewAssignment(ClickEvent<Button> buttonClickEvent) {
        assignmentForm.prepareForm(new Assignment(),
                crmService.findAllConsultants(),
                crmService.findAllContacts(""),
                crmService.findAllConsidContracts());
        assignmentDialog.open();
    }

    private void setupDialog() {
        assignmentForm = new AssignmentForm();
        assignmentForm.addSaveListener(this::saveAssignment);
        assignmentForm.addCloseListener(e -> closeDialog());
        assignmentForm.addArchiveListener(this::archiveAssignment);

        assignmentDialog.add(assignmentForm);
    }

    private void saveAssignment(AssignmentForm.SaveEvent saveEvent) {
        crmService.saveAssignment(saveEvent.getAssignment());
        updateGridContent();
        assignmentDialog.close();
    }

    private void archiveAssignment(AssignmentForm.ArchiveEvent archiveEvent) {
        crmService.archiveAssignment(archiveEvent.getAssignment());
        updateGridContent();
        assignmentDialog.close();

        Notification notification = Notification.show("Assignment Archived!", 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void closeDialog() { assignmentDialog.close(); }

    private void configureGrid() {
        grid.addClassName("assignment-grid");
        grid.setSizeFull();
        grid.addColumn(assignment -> assignment.getConsultant().toString()).setHeader("Consultant");
        grid.addColumn(assignment -> assignment.getCustomerContact().toString()).setHeader("Contact/Company");
        grid.addColumn(Assignment::getStartDate).setHeader("Start Date");
        grid.addColumn(Assignment::getEndDate).setHeader("End Date");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> editAssignment(event.getItem()));
    }

    private void editAssignment(Assignment assignment) {
        assignmentForm.prepareForm(assignment,
                crmService.findAllConsultants(),
                crmService.findAllContacts(""),
                crmService.findAllConsidContracts());
        assignmentDialog.open();
    }

    private void updateGridContent() { grid.setItems(crmService.findAllAssignments()); }

}
