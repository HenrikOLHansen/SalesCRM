package com.example.application.views.form;

import com.example.application.data.entity.Assignment;
import com.example.application.data.entity.ConsidContact;
import com.example.application.data.entity.Consultant;
import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.util.List;

import static com.example.application.data.entity.AbstractEntity.DESC_MAX_LENGTH;

public class AssignmentForm extends FormLayout {

    BeanValidationBinder<Assignment> binder = new BeanValidationBinder<>(Assignment.class);

    ComboBox<Consultant> consultant = new ComboBox<>("Consultant");
    ComboBox<Contact> customerContact = new ComboBox<>("Contact: Customer");
    ComboBox<Contact> additionalContact = new ComboBox<>("Contact: Additional");
    ComboBox<ConsidContact> considContact = new ComboBox<>("Consid Contact");
    DatePicker startDate = new DatePicker("Start Date");
    DatePicker endDate = new DatePicker("End Date");
    TextArea description = new TextArea("Description");

    Button saveButton = new Button("Save", new Icon(VaadinIcon.FILE));
    Button cancelButton = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
    Button completeButton = new Button("Archive", new Icon(VaadinIcon.ARCHIVE));

    public AssignmentForm() {
        addClassName("assignment-form");
        binder.bindInstanceFields(this);

        customerContact.setItemLabelGenerator(Contact::toString);
        additionalContact.setItemLabelGenerator(Contact::toString);
        considContact.setItemLabelGenerator(ConsidContact::getFirstName);
        consultant.setItemLabelGenerator(Consultant::toString);
        startDate.setValue(LocalDate.now());
        description.setPlaceholder("Enter description here...");
        description.setMaxLength(DESC_MAX_LENGTH);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + DESC_MAX_LENGTH));
        setColspan(description, 2);

        add(customerContact,
                considContact,
                startDate,
                endDate,
                consultant,
                description,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new AssignmentForm.CloseEvent(this)));
        completeButton.addClickListener(event -> fireEvent(new AssignmentForm.ArchiveEvent(this, binder.getBean())));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton, completeButton);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new AssignmentForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void prepareForm(Assignment assignment,
                            List<Consultant> allConsultants,
                            List<Contact> allContacts,
                            List<ConsidContact> allConsidContacts) {
        consultant.setItems(allConsultants);
        if(assignment.getConsultant() != null) {
            consultant.setValue(assignment.getConsultant());
        }
        customerContact.setItems(allContacts);
        if(assignment.getCustomerContact() != null) {
            customerContact.setValue(assignment.getCustomerContact());
        }
        additionalContact.setItems(allContacts);
        if(assignment.getAdditionalContact() != null) {
            additionalContact.setValue(assignment.getCustomerContact());
        }
        considContact.setItems(allConsidContacts);
        if(assignment.getConsidContact() != null) {
            considContact.setValue(assignment.getConsidContact());
        }
        binder.setBean(assignment);
    }

    public static abstract class AssignmentFormEvent extends ComponentEvent<AssignmentForm> {
        private final Assignment assignment;

        protected AssignmentFormEvent(AssignmentForm source, Assignment assignment) {
            super(source, false);
            this.assignment = assignment;
        }

        public Assignment getAssignment() { return assignment; }
    }

    public static class SaveEvent extends AssignmentForm.AssignmentFormEvent {
        SaveEvent(AssignmentForm source, Assignment assignment) { super(source, assignment); }
    }

    public static class CloseEvent extends AssignmentForm.AssignmentFormEvent {
        CloseEvent(AssignmentForm source) { super(source, null); }
    }

    public static class ArchiveEvent extends AssignmentForm.AssignmentFormEvent {
        ArchiveEvent(AssignmentForm source, Assignment assignment) { super(source, assignment); }
    }

    public Registration addSaveListener(ComponentEventListener<AssignmentForm.SaveEvent> listener) {
        return addListener(AssignmentForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<AssignmentForm.CloseEvent> listener) {
        return addListener(AssignmentForm.CloseEvent.class, listener);
    }

    public Registration addArchiveListener(ComponentEventListener<AssignmentForm.ArchiveEvent> listener) {
        return addListener(AssignmentForm.ArchiveEvent.class, listener);
    }
}
