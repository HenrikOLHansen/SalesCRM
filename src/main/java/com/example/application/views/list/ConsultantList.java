package com.example.application.views.list;

import com.example.application.data.entity.Consultant;
import com.example.application.data.entity.Lead;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.example.application.views.form.ConsultantForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Consultants | Sales CRM")
@Route(value = "consultants", layout = MainLayout.class)
public class ConsultantList extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<Consultant> grid = new Grid<>(Consultant.class, false);
    ConsultantForm consultantForm;
    Dialog consultantDialog = new Dialog();
    List<Lead> allLeads = new ArrayList<>();

    private final CrmService crmService;

    public ConsultantList(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Consultants");
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
        filterText.setPlaceholder("Filter by skill...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateGridContent());

        Button createNewConsultantButton = new Button("New Consultant");
        createNewConsultantButton.addClickListener(this::createNewConsultant);
        return new HorizontalLayout(filterText, createNewConsultantButton);
    }

    private void createNewConsultant(ClickEvent<Button> buttonClickEvent) {
        consultantForm.prepareForm(new Consultant(), crmService.findAllSkills());
        consultantDialog.open();
    }

    private void setupDialog() {
        consultantForm = new ConsultantForm();
        consultantForm.addSaveListener(this::saveConsultant);
        consultantForm.addCloseListener(e -> closeDialog());

        consultantDialog.add(consultantForm);
    }

    private void saveConsultant(ConsultantForm.SaveEvent saveEvent) {
        crmService.saveConsultant(saveEvent.getConsultant());
        updateGridContent();
        consultantDialog.close();
    }

    private void closeDialog() { consultantDialog.close(); }

    private void editConsultant(Consultant consultant) {
        consultantForm.prepareForm(consultant, crmService.findAllSkills());
        consultantDialog.open();
    }

    private void configureGrid() {
        grid.addClassNames("consultant-grid");
        grid.setSizeFull();

        grid.addColumn(consultant -> consultant.getFirstName() + " " + consultant.getLastName()).setHeader("Full Name");
        grid.addComponentColumn(this::assignmentStatus).setHeader("Assignment");
        grid.addComponentColumn(this::leadStatus).setHeader("Leads");
        grid.addColumn(Consultant::getShortSkillList).setHeader("Skills (max. 4 shown)");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> editConsultant(event.getItem()));
    }

    private Span assignmentStatus(Consultant consultant) {
        String theme = consultant.getAssignments().isEmpty() ? "badge contrast" : "badge success";
        String status = consultant.getAssignments().isEmpty() ? "No Assignment" : "On Assignment";
        Span span = new Span(status);
        span.getElement().getThemeList().add(theme);
        return span;
    }

    private Span leadStatus(Consultant consultant) {
        List<Lead> leads = allLeads.stream().filter(lead -> lead.getConsultants().contains(consultant)).toList();

        String theme = "badge contrast";
        String status = "No Leads";
        if(!leads.isEmpty()) {
            theme = "badge success";
            status = leads.size() + " Lead";
            if(leads.size() > 1) {
                status += "s";
            }
        }

        Span span = new Span(status);
        span.getElement().getThemeList().add(theme);
        return span;
    }

    private void updateGridContent() {
        allLeads = crmService.findAllLeads();
        grid.setItems(crmService.findConsultantsBySkill(filterText.getValue()));
    }

}
