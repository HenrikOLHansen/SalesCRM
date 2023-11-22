package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.example.application.views.form.CompanyForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Companies | Sales CRM")
@Route(value = "companies", layout = MainLayout.class)
public class CompanyList extends VerticalLayout {

    Grid<Company> grid = new Grid<>(Company.class);
    CompanyForm companyForm;
    Dialog companyDialog = new Dialog();

    private CrmService crmService;

    public CompanyList(CrmService crmService) {
        this.crmService = crmService;
        H2 header = new H2("Companies");
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
        Button createNewCompanyButton = new Button("New Company");
        createNewCompanyButton.addClickListener(this::createNewCompany);
        return new HorizontalLayout(createNewCompanyButton);
    }

    private void createNewCompany(ClickEvent<Button> buttonClickEvent) {
        companyForm.prepareForm(new Company());
        companyDialog.open();
    }

    private void configureGrid() {
        grid.addClassNames("company-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> editCompany(event.getItem()));
    }

    private void setupDialog() {
        companyForm = new CompanyForm();
        companyForm.addSaveListener(this::saveCompany);
        companyForm.addCloseListener(e -> closeDialog());

        companyDialog.add(companyForm);
    }

    private void closeDialog() { companyDialog.close(); }

    private void saveCompany(CompanyForm.SaveEvent saveEvent) {
        crmService.saveCompany(saveEvent.getCompany());
        updateGridContent();
        companyDialog.close();
    }

    private void editCompany(Company company) {
        companyForm.prepareForm(company);
        companyDialog.open();
    }

    private void updateGridContent() {
        grid.setItems(crmService.findAllCompanies());
    }

}
