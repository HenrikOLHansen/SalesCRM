package com.consid.application.views.form;

import com.consid.application.data.entity.AbstractEntity;
import com.consid.application.data.entity.Contact;
import com.consid.application.data.entity.Task;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;

public class TaskForm extends FormLayout {

    BeanValidationBinder<Task> binder = new BeanValidationBinder<>(Task.class);

    ComboBox<Contact> contact = new ComboBox<>("Contact");
    DatePicker dueDate = new DatePicker("Due Date");
    TextField link = new TextField("Link");
    TextArea description = new TextArea("Description");

    Button saveButton = new Button("Save", new Icon(VaadinIcon.FILE));
    Button cancelButton = new Button("Cancel", new Icon(VaadinIcon.CLOSE));

    public TaskForm() {
        addClassName("task-form");
        binder.bindInstanceFields(this);

        contact.setItemLabelGenerator(Contact::toString);
        dueDate.setValue(LocalDate.now());

        setColspan(link, 2);

        description.setPlaceholder("Enter description here...");
        description.setMaxLength(AbstractEntity.DESC_MAX_LENGTH);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + AbstractEntity.DESC_MAX_LENGTH));
        setColspan(description, 2);

        add(contact,
                dueDate,
                link,
                description,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new TaskForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    public void prepareForm(Contact selectedContact, UserDetails currentUser) {
        Task task = new Task();
        task.setUsername(currentUser.getUsername());
        task.setDueDate(LocalDate.now());
        task.setContact(selectedContact);
        contact.setItems(selectedContact);
        contact.setReadOnly(true);
        contact.setValue(selectedContact);

        binder.setBean(task);
    }

    public void prepareForm(List<Contact> contacts, UserDetails currentUser) {
        Task task = new Task();
        task.setUsername(currentUser.getUsername());
        task.setDueDate(LocalDate.now());
        contact.setItems(contacts);

        binder.setBean(task);
    }

    public void prepareForm(Task task, UserDetails currentUser) {
        task.setUsername(currentUser.getUsername());
        contact.setItems(task.getContact());
        contact.setValue(task.getContact());
        contact.setReadOnly(true);
        binder.setBean(task);
    }

    public void prepareForm(List<Contact> contacts, Contact selectedContact) {
        Task task = new Task();
        task.setDueDate(LocalDate.now());
        contact.setItems(contacts);
        contact.setValue(selectedContact);
        contact.setReadOnly(false);

        binder.setBean(task);
    }


    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new TaskForm.SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class TaskFormEvent extends ComponentEvent<TaskForm> {
        private final Task task;

        protected TaskFormEvent(TaskForm source, Task task) {
            super(source, false);
            this.task = task;
        }

        public Task getTask() { return task; }
    }

    public static class SaveEvent extends TaskForm.TaskFormEvent {
        SaveEvent(TaskForm source, Task task) { super(source, task); }
    }

    public static class CloseEvent extends TaskForm.TaskFormEvent {
        CloseEvent(TaskForm source) { super(source, null); }
    }

    public Registration addSaveListener(ComponentEventListener<TaskForm.SaveEvent> listener) {
        return addListener(TaskForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<TaskForm.CloseEvent> listener) {
        return addListener(TaskForm.CloseEvent.class, listener);
    }

}
