package com.consid.application.views.form;

import com.consid.application.data.entity.*;
import com.consid.application.data.entity.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LeadForm extends FormLayout {

    BeanValidationBinder<Lead> binder = new BeanValidationBinder<>(Lead.class);

    ComboBox<Contact> customerContact = new ComboBox<>("Customer Contact");
    ComboBox<Contact> additionalContact = new ComboBox<>("Additional Contact");
    ComboBox<ConsidContact> considContact = new ComboBox<>("Main Consid Contact");
    Scroller consultantScroller;
    MultiSelectComboBox<Consultant> consultantSelector;
    DatePicker startDate = new DatePicker("Start Date");
    DatePicker endDate = new DatePicker("End Date");
    TextArea description = new TextArea("Description");
    ComboBox<LeadStatus> leadStatus = new ComboBox<>("Status");

    Button saveButton = new Button("Save");
    Button assignmentButton = new Button("Create Assignment(s)");
    Button cancelButton = new Button("Cancel");

    public LeadForm() {
        addClassName("lead-form");

        binder.bindInstanceFields(this);

        customerContact.setItemLabelGenerator(Contact::toString);
        additionalContact.setItemLabelGenerator(Contact::toString);
        considContact.setItemLabelGenerator(ConsidContact::getFirstName);
        leadStatus.setItemLabelGenerator(LeadStatus::toString);

        description.setWidthFull();
        description.setMinHeight("100px");
        description.setMaxHeight("300px");
        description.setMaxLength(AbstractEntity.DESC_MAX_LENGTH);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + AbstractEntity.DESC_MAX_LENGTH));
        setColspan(description, 2);

        configureConsultants();

        add(customerContact,
                additionalContact,
                startDate,
                endDate,
                consultantScroller,
                considContact,
                leadStatus,
                description,
                createButtonsLayout());
    }

    private void configureConsultants() {
        consultantSelector = new MultiSelectComboBox<>();
        consultantSelector.setLabel("Consultant(s)");
        consultantSelector.setItemLabelGenerator(Consultant::toString);

        consultantScroller = new Scroller(new Div(consultantSelector));
        consultantScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        consultantScroller.setMaxHeight("200px");
        consultantSelector.setWidthFull();
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        assignmentButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        assignmentButton.setEnabled(false);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        assignmentButton.addClickListener(event -> convertToAssignment());
        cancelButton.addClickListener(event -> fireEvent(new LeadForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, assignmentButton, cancelButton);
    }

    private void convertToAssignment() {
        if(binder.isValid()) {
            Set<Consultant> selectedConsultants = consultantSelector.getSelectedItems();
            binder.getBean().setConsultants(new ArrayList<>(selectedConsultants));
            fireEvent(new LeadForm.AssignEvent(this, binder.getBean()));
        }
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            Set<Consultant> selectedConsultants = consultantSelector.getSelectedItems();
            binder.getBean().setConsultants(new ArrayList<>(selectedConsultants));
            fireEvent(new LeadForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void prepareForm(Lead lead, List<Contact> allContacts, List<ConsidContact> allConsidContacts, List<Consultant> allConsultants) {
        customerContact.setItems(allContacts);
        additionalContact.setItems(allContacts);
        considContact.setItems(allConsidContacts);
        if(lead.getCustomerContact() != null) {
            customerContact.setValue(lead.getCustomerContact());
            assignmentButton.setEnabled(true);
        }
        if(lead.getAdditionalContact() != null) {
            additionalContact.setValue(lead.getAdditionalContact());
        }
        if(lead.getConsidContact() != null) {
            considContact.setValue(lead.getConsidContact());
        }

        leadStatus.setItems(LeadStatus.values());

        updateConsultants(lead, allConsultants);

        binder.setBean(lead);
    }

    private void updateConsultants(Lead lead, List<Consultant> allConsultants) {
        List<Consultant> selectedConsultants = lead == null || lead.getConsultants() == null ? new ArrayList<>() : lead.getConsultants();

        consultantSelector.setItems(allConsultants);
        consultantSelector.select(selectedConsultants);
    }

    // Events
    public static abstract class LeadFormEvent extends ComponentEvent<LeadForm> {
        private final Lead lead;

        protected LeadFormEvent(LeadForm source, Lead lead) {
            super(source, false);
            this.lead = lead;
        }

        public Lead getLead() {
            return lead;
        }
    }

    public static class SaveEvent extends LeadForm.LeadFormEvent {
        SaveEvent(LeadForm source, Lead lead) {
            super(source, lead);
        }
    }

    public static class CloseEvent extends LeadForm.LeadFormEvent {
        CloseEvent(LeadForm source) {
            super(source, null);
        }
    }

    public static class AssignEvent extends LeadForm.LeadFormEvent {
        AssignEvent(LeadForm source, Lead lead) { super(source, lead); }
    }

    public Registration addSaveListener(ComponentEventListener<LeadForm.SaveEvent> listener) {
        return addListener(LeadForm.SaveEvent.class, listener);
    }

    public Registration addConvertToAssignmentListener(ComponentEventListener<LeadForm.AssignEvent> listener) {
        return addListener(LeadForm.AssignEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<LeadForm.CloseEvent> listener) {
        return addListener(LeadForm.CloseEvent.class, listener);
    }
}
