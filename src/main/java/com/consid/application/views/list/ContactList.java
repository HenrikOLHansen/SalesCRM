package com.consid.application.views.list;

import com.consid.application.data.entity.Contact;
import com.consid.application.data.service.CrmService;
import com.consid.application.views.MainLayout;
import com.consid.application.views.form.ContactForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Contacts | Sales CRM")
@Route(value = "contacts", layout = MainLayout.class)
public class ContactList extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<Contact> grid = new Grid<>(Contact.class);
    Dialog contactDialog = new Dialog();
    ContactForm contactForm;
    CrmService service;

    public ContactList(CrmService crmService) {
        this.service = crmService;
        setSpacing(false);

        H2 header = new H2("Contacts");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureGrid();
        add(createToolbar(), grid);

        setupDialog();

        updateGridContent();
    }

    private Component createToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateGridContent());

        Button createNewContactButton = new Button("New Contact");
        createNewContactButton.addClickListener(this::createNewContact);
        return new HorizontalLayout(filterText, createNewContactButton);
    }

    private void createNewContact(ClickEvent<Button> buttonClickEvent) {
        contactForm.setContact(new Contact());
        contactDialog.open();
    }


    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email", "phoneNumber");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
        grid.setRowsDraggable(true);
    }

    private void editContact(Contact contact) {
        contactForm.setContact(contact);
        contactDialog.open();
    }

    private void setupDialog() {
        contactForm = new ContactForm(service.findAllCompanies());
        contactForm.addSaveListener(this::saveContact);
        contactForm.addCloseListener(e -> closeDialog());

        contactDialog.add(contactForm);
    }

    private void closeDialog() {
        contactDialog.close();
    }

    private void saveContact(ContactForm.SaveEvent saveEvent) {
        service.saveContact(saveEvent.getContact());
        updateGridContent();
        contactDialog.close();
    }

    private void updateGridContent() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

}
