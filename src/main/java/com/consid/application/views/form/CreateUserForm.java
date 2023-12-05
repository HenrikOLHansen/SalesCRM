package com.consid.application.views.form;

import com.consid.application.data.entity.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public class CreateUserForm extends FormLayout {
    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField confirmPassword = new PasswordField("Confirm password");
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    public CreateUserForm() {
        addClassName("create-user-form");
        binder.bindInstanceFields(this);

        add(firstName,
                lastName,
                email,
                password,
                confirmPassword,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CreateUserForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new CreateUserForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setUser(final User user) {
        binder.setBean(user);
    }

    // Events
    public static class SaveEvent extends CreateUserForm.CreateUserFormEvent {
        SaveEvent(final CreateUserForm source, final User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends CreateUserForm.CreateUserFormEvent {
        CloseEvent(CreateUserForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<CreateUserForm.SaveEvent> listener) {
        return addListener(CreateUserForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CreateUserForm.CloseEvent> listener) {
        return addListener(CreateUserForm.CloseEvent.class, listener);
    }

    public static abstract class CreateUserFormEvent extends ComponentEvent<CreateUserForm> {
        private final User user;

        protected CreateUserFormEvent(final CreateUserForm source, final User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}