package com.consid.application.views.form;

import com.consid.application.data.entity.AbstractEntity;
import com.consid.application.data.entity.CompletedTask;
import com.consid.application.data.entity.Task;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

public class CompleteTaskForm extends FormLayout {

    BeanValidationBinder<CompletedTask> binder = new BeanValidationBinder<>(CompletedTask.class);
    private Task existingTask;

    TextArea notes = new TextArea("Notes");

    Button saveButton = new Button("Save", new Icon(VaadinIcon.FILE));
    Button cancelButton = new Button("Cancel", new Icon(VaadinIcon.CLOSE));

    public CompleteTaskForm() {
        addClassName("complete-task-form");

        binder.bindInstanceFields(this);

        notes.setPlaceholder("Enter notes here...");
        notes.setMaxLength(AbstractEntity.DESC_MAX_LENGTH);
        notes.setValueChangeMode(ValueChangeMode.EAGER);
        notes.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + AbstractEntity.DESC_MAX_LENGTH));

        add(notes, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CompleteTaskForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new CompleteTaskForm.SaveEvent(this, binder.getBean(), existingTask));
        }
    }

    public void prepareForm(Task existingTask) {
        this.existingTask = existingTask;
        CompletedTask completedTask = new CompletedTask(existingTask);
        binder.setBean(completedTask);
    }

    public static abstract class CompleteTaskFormEvent extends ComponentEvent<CompleteTaskForm> {
        private final CompletedTask completedTask;
        private final Task existingTask;

        protected CompleteTaskFormEvent(CompleteTaskForm source, CompletedTask completedTask, Task existingTask) {
            super(source, false);
            this.completedTask = completedTask;
            this.existingTask = existingTask;
        }

        public CompletedTask getCompletedTask() { return completedTask; }
        public Task getExistingTask() { return existingTask; }
    }

    public static class SaveEvent extends CompleteTaskForm.CompleteTaskFormEvent {
        SaveEvent(CompleteTaskForm source, CompletedTask completedTask, Task existingTask) { super(source, completedTask, existingTask); }
    }

    public static class CloseEvent extends CompleteTaskForm.CompleteTaskFormEvent {
        CloseEvent(CompleteTaskForm source) { super(source, null, null); }
    }

    public Registration addSaveListener(ComponentEventListener<CompleteTaskForm.SaveEvent> listener) {
        return addListener(CompleteTaskForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CompleteTaskForm.CloseEvent> listener) {
        return addListener(CompleteTaskForm.CloseEvent.class, listener);
    }
}
