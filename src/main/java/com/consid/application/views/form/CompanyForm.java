package com.consid.application.views.form;

import com.consid.application.data.entity.Company;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

public class CompanyForm extends FormLayout {

    BeanValidationBinder<Company> binder = new BeanValidationBinder<>(Company.class);

    TextField name = new TextField("Company Name");

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    public CompanyForm() {
        addClassName("company-form");

        binder.bindInstanceFields(this);

        add(name, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CompanyForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new CompanyForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void prepareForm(Company company) {
        binder.setBean(company);
    }

    public static abstract class CompanyFormEvent extends ComponentEvent<CompanyForm> {
        private final Company company;

        protected CompanyFormEvent(CompanyForm source, Company company) {
            super(source, false);
            this.company = company;
        }

        public Company getCompany() { return company; }
    }

    public static class SaveEvent extends CompanyForm.CompanyFormEvent {
        SaveEvent(CompanyForm source, Company company) { super(source, company); }
    }

    public static class CloseEvent extends CompanyForm.CompanyFormEvent {
        CloseEvent(CompanyForm source) { super(source, null); }
    }

    public Registration addSaveListener(ComponentEventListener<CompanyForm.SaveEvent> listener) {
        return addListener(CompanyForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CompanyForm.CloseEvent> listener) {
        return addListener(CompanyForm.CloseEvent.class, listener);
    }
}
