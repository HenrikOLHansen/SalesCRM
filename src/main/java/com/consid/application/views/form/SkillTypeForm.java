package com.consid.application.views.form;

import com.consid.application.data.entity.SkillType;
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

public class SkillTypeForm extends FormLayout {

    BeanValidationBinder<SkillType> binder = new BeanValidationBinder<>(SkillType.class);

    TextField type = new TextField("Skill Type");

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    public SkillTypeForm() {
        addClassName("skill-type-form");

        binder.bindInstanceFields(this);

        add(type, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new SkillTypeForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SkillTypeForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setSkillType(SkillType skillType) {
        binder.setBean(skillType);
    }

    // Events
    public static abstract class SkillTypeFormEvent extends ComponentEvent<SkillTypeForm> {
        private SkillType skillType;

        protected SkillTypeFormEvent(SkillTypeForm source, SkillType skillType) {
            super(source, false);
            this.skillType = skillType;
        }

        public SkillType getSkillType() {
            return skillType;
        }
    }

    public static class SaveEvent extends SkillTypeForm.SkillTypeFormEvent {
        SaveEvent(SkillTypeForm source, SkillType skillType) {
            super(source, skillType);
        }
    }

    public static class CloseEvent extends SkillTypeForm.SkillTypeFormEvent {
        CloseEvent(SkillTypeForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SkillTypeForm.SaveEvent> listener) {
        return addListener(SkillTypeForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<SkillTypeForm.CloseEvent> listener) {
        return addListener(SkillTypeForm.CloseEvent.class, listener);
    }
}
