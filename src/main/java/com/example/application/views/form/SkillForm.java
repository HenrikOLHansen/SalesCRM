package com.example.application.views.form;

import com.example.application.data.entity.Skill;
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

public class SkillForm extends FormLayout {

    BeanValidationBinder<Skill> binder = new BeanValidationBinder<>(Skill.class);

    TextField name = new TextField("Skill Name");

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    public SkillForm() {
        addClassName("skill-form");

        binder.bindInstanceFields(this);

        add(name, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new SkillForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SkillForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setSkill(Skill skill) {
        binder.setBean(skill);
    }

    // Events
    public static abstract class SkillFormEvent extends ComponentEvent<SkillForm> {
        private Skill skill;

        protected SkillFormEvent(SkillForm source, Skill skill) {
            super(source, false);
            this.skill = skill;
        }

        public Skill getSkill() {
            return skill;
        }
    }

    public static class SaveEvent extends SkillForm.SkillFormEvent {
        SaveEvent(SkillForm source, Skill skill) {
            super(source, skill);
        }
    }

    public static class CloseEvent extends SkillForm.SkillFormEvent {
        CloseEvent(SkillForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SkillForm.SaveEvent> listener) {
        return addListener(SkillForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<SkillForm.CloseEvent> listener) {
        return addListener(SkillForm.CloseEvent.class, listener);
    }
}
