package com.example.application.views.form;

import com.example.application.data.entity.Consultant;
import com.example.application.data.entity.Skill;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConsultantForm extends FormLayout {

    BeanValidationBinder<Consultant> binder = new BeanValidationBinder<>(Consultant.class);

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    Scroller skillScroller;
    CheckboxGroup<Skill> skillSelector;

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    public ConsultantForm() {
        addClassName("consultant-form");

        setResponsiveSteps(new ResponsiveStep("0", 1));

        binder.bindInstanceFields(this);

        configureSkills();

        add(firstName,
                lastName,
                skillScroller,
                createButtonsLayout());

    }

    private void configureSkills() {
        skillSelector = new CheckboxGroup<>();
        skillSelector.setLabel("Select Skills");
        skillSelector.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        skillScroller = new Scroller(
                new Div(skillSelector));
        skillScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        skillScroller.setHeight("200px");
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new ConsultantForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            Set<Skill> selectedSkills = skillSelector.getSelectedItems();
            binder.getBean().setSkills(new ArrayList<>(selectedSkills));
            fireEvent(new ConsultantForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void prepareForm(Consultant consultant, List<Skill> allSkills) {
        binder.setBean(consultant);
        updateSkills(allSkills);
    }

    private void updateSkills(List<Skill> allSkills) {
        List<Skill> selectedSkills = binder.getBean() == null ? new ArrayList<>() : binder.getBean().getSkills();

        skillSelector.setItems(allSkills);
        skillSelector.select(selectedSkills);
    }

    // Events
    public static abstract class ConsultantFormEvent extends ComponentEvent<ConsultantForm> {
        private final Consultant consultant;

        protected ConsultantFormEvent(ConsultantForm source, Consultant consultant) {
            super(source, false);
            this.consultant = consultant;
        }

        public Consultant getConsultant() {
            return consultant;
        }
    }

    public static class SaveEvent extends ConsultantForm.ConsultantFormEvent {
        SaveEvent(ConsultantForm source, Consultant consultant) {
            super(source, consultant);
        }
    }

    public static class CloseEvent extends ConsultantForm.ConsultantFormEvent {
        CloseEvent(ConsultantForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<ConsultantForm.SaveEvent> listener) {
        return addListener(ConsultantForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<ConsultantForm.CloseEvent> listener) {
        return addListener(ConsultantForm.CloseEvent.class, listener);
    }
}
