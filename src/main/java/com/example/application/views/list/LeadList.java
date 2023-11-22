package com.example.application.views.list;

import com.example.application.data.entity.Assignment;
import com.example.application.data.entity.Consultant;
import com.example.application.data.entity.Lead;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.example.application.views.form.LeadForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

@PageTitle("Leads | Sales CRM")
@Route(value = "leads", layout = MainLayout.class)
public class LeadList extends VerticalLayout {

    ComboBox<Consultant> consultantFilter = new ComboBox<>();
    Grid<Lead> grid = new Grid<>(Lead.class, false);
    LeadForm leadForm;
    Dialog leadDialog = new Dialog();

    private final CrmService crmService;

    public LeadList(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Leads");
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
        consultantFilter.setPlaceholder("Filter by consultant");
        consultantFilter.setClearButtonVisible(true);
        consultantFilter.addValueChangeListener(e -> updateGridContent());
        consultantFilter.setItems(crmService.findAllConsultants());

        Button createNewLeadButton = new Button("New Lead");
        createNewLeadButton.addClickListener(this::createNewLead);
        return new HorizontalLayout(consultantFilter, createNewLeadButton);
    }

    private void createNewLead(ClickEvent<Button> buttonClickEvent) {
        leadForm.prepareForm(new Lead(),
                crmService.findAllContacts(""),
                crmService.findAllConsidContracts(),
                crmService.findAllConsultants());
        leadDialog.open();
    }

    private void setupDialog() {
        leadForm = new LeadForm();
        leadForm.addSaveListener(this::saveLead);
        leadForm.addConvertToAssignmentListener(this::createAssignment);
        leadForm.addCloseListener(e -> closeDialog());

        leadDialog.add(leadForm);
    }

    private void createAssignment(LeadForm.AssignEvent assignEvent) {
        Lead lead = assignEvent.getLead();

        for(Consultant consultant : lead.getConsultants()) {
            Assignment assignment = new Assignment(lead, consultant);
            crmService.saveAssignment(assignment);
        }

        crmService.deleteLead(lead);
        updateGridContent();
        leadDialog.close();

        Notification notification = Notification.show("Assignment created!", 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void saveLead(LeadForm.SaveEvent saveEvent) {
        crmService.saveLead(saveEvent.getLead());
        updateGridContent();
        leadDialog.close();
    }

    private void closeDialog() { leadDialog.close(); }

    private void editLead(Lead lead) {
        leadForm.prepareForm(lead,
                crmService.findAllContacts(""),
                crmService.findAllConsidContracts(),
                crmService.findAllConsultants());
        leadDialog.open();
    }

    private void configureGrid() {
        grid.addClassNames("lead-grid");
        grid.setSizeFull();
        grid.addColumn(lead -> lead.getCustomerContact().getCompany().getName()).setHeader("Company");
        grid.addColumn(lead -> lead.getCustomerContact().getFirstName() + " " + lead.getCustomerContact().getLastName()).setHeader("Contact");
        grid.addComponentColumn(this::consultantStatus).setHeader("Consultants");
        grid.addColumn(this::getShortDescription).setHeader("Description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> editLead(event.getItem()));
    }

    private Span consultantStatus(Lead lead) {
        String theme = "badge contrast";
        String status = "None";
        if(!lead.getConsultants().isEmpty()) {
            theme = "badge success";
            status = "Yes: " + lead.getConsultants().size();
        }

        Span span = new Span(status);
        span.getElement().getThemeList().add(theme);
        return span;
    }

    private String getShortDescription(Lead lead) {
        String desc = lead.getDescription();
        if(desc.length() < 20) {
            return desc;
        }
        return desc.substring(0, 20) + "...";
    }

    private void updateGridContent() {
        List<Lead> leads = crmService.findAllLeads();
        if(consultantFilter.getValue() != null) {
            leads = leads.stream()
                    .filter(lead -> lead.getConsultants().contains(consultantFilter.getValue()))
                    .toList();
        }

        grid.setItems(leads);
    }

}
