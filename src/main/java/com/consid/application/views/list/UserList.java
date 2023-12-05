package com.consid.application.views.list;

import com.consid.application.data.entity.User;
import com.consid.application.data.service.UserService;
import com.consid.application.views.MainLayout;
import com.consid.application.views.form.CreateUserForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Users | Sales CRM")
@Route(value = "users", layout = MainLayout.class)
public class UserList extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<User> grid = new Grid<>(User.class);
    GridListDataView<User> dataView;
    Dialog createUserDialog = new Dialog();
    CreateUserForm createUserForm;

    private final UserService service;


    public UserList(final UserService userService) {
        this.service = userService;
        setSpacing(false);

        H2 header = new H2("Users");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureGrid();
        add(createToolbar(), grid);

        setupDialog();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setRowsDraggable(true);

        dataView = grid.setItems(service.getUsers());
        dataView.addFilter(user -> {
            var filter = filterText.getValue();
            if (filter.isEmpty()) {
                return true;
            }
            return matchesFilter(user.getFirstName(), filter)
                    || matchesFilter(user.getLastName(), filter)
                    || matchesFilter(user.getEmail(), filter);
        });
    }

    private void setupDialog() {
        createUserForm = new CreateUserForm();
        createUserForm.addSaveListener(this::saveUser);
        createUserForm.addCloseListener(e -> closeDialog());

        createUserDialog.add(createUserForm);
    }

    private Component createToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> dataView.refreshAll());

        var createNewContactButton = new Button("New Contact");
        createNewContactButton.addClickListener(this::createNewContact);
        return new HorizontalLayout(filterText, createNewContactButton);
    }

    private boolean matchesFilter(final Object object, final String filter) {
        return object.toString().toLowerCase().contains(filter.toLowerCase());
    }

    private void closeDialog() {
        createUserDialog.close();
    }

    private void saveUser(final CreateUserForm.SaveEvent saveEvent) {
        service.registerUser(saveEvent.getUser());
        dataView.refreshAll();
        createUserDialog.close();
    }

    private void createNewContact(final ClickEvent<Button> buttonClickEvent) {
        createUserForm.setUser(new User());
        createUserDialog.open();
    }
}
