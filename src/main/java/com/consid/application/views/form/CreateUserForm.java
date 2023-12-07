package com.consid.application.views.form;

import com.consid.application.data.entity.Role;
import com.consid.application.data.entity.Skill;
import com.consid.application.data.entity.User;
import com.consid.application.data.exceptions.UserException;
import com.consid.application.data.repository.RoleRepository;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class CreateUserForm extends FormLayout {
    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    private final RoleRepository roleRepository;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField confirmPassword = new PasswordField("Confirm password");
    CheckboxGroup<Role> rolesSelector;
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");

    public CreateUserForm(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;

        addClassName("create-user-form");
        binder.bindInstanceFields(this);

        configureRoles();

        add(firstName,
                lastName,
                email,
                password,
                confirmPassword,
                rolesSelector,
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

    private void configureRoles() {
        rolesSelector = new CheckboxGroup<>();
        rolesSelector.setLabel("Select Roles");
        rolesSelector.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        rolesSelector.setItems(roleRepository.findAll());
    }

    private void validateAndSave() {
        try {
            if (binder.isValid()) {
                Set<Role> roles = rolesSelector.getSelectedItems();
                log.info("Selected roles: {}", roles);
                binder.getBean().setRoles(roles.stream().toList());
                fireEvent(new CreateUserForm.SaveEvent(this, binder.getBean()));
            }
        } catch (UserException e) {
            new Notification(
                    "Something went wrong.: " + e.getMessage(), 3000,
                    Notification.Position.TOP_CENTER
            ).open();
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
