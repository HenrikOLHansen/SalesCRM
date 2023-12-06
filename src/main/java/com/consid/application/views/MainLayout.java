package com.consid.application.views;

import com.consid.application.data.entity.Contact;
import com.consid.application.data.service.CrmService;
import com.consid.application.security.SecurityService;
import com.consid.application.views.form.CreateUserForm;
import com.consid.application.views.form.TaskForm;
import com.consid.application.views.list.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final CrmService crmService;
    TaskForm taskForm;
    Dialog taskDialog = new Dialog();

    public MainLayout(final SecurityService securityService, final CrmService crmService) {
        this.securityService = securityService;
        this.crmService = crmService;
        createDrawer();
    }

    @NotNull
    private void createDrawer() {

        var considLogo = new Image("images/consid-logo-nocolor.png", "Consid Logo");
        considLogo.setMaxHeight("50px");

        var header = getHeader(considLogo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToDrawer(getSideNav());
        addToNavbar(header);

        setupTaskDialog();
    }

    @NotNull
    private HorizontalLayout getHeader(final Image considLogo) {
        var logoText = new H1("Sales CRM");
        logoText.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String username = securityService.getAuthenticatedUser().getUsername();
        var logoutButton = new Button("Log out " + username, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), considLogo, logoText, logoutButton);
        header.expand(logoText);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return header;
    }

    @NotNull
    private VerticalLayout getSideNav() {
        var topNav = new SideNav("Status");
        topNav.addItem(new SideNavItem("Status", StatusPage.class, VaadinIcon.BAR_CHART_V.create()),
                new SideNavItem("Assignments", AssignmentList.class, VaadinIcon.CLIPBOARD_USER.create()));

        var taskNav = getTaskNav();
        var dataNav = getDataNav();
        var archiveNav = getArchiveNav();
        var userNav = getUserNav();

        var navWrapper = new VerticalLayout(topNav, dataNav, taskNav, archiveNav, userNav);
        navWrapper.setSpacing(true);
        navWrapper.setSizeUndefined();

        return navWrapper;
    }

    @NotNull
    private SideNav getTaskNav() {
        var taskNav = new SideNav("Tasks");
        taskNav.addItem(new SideNavItem("Task List", TaskList.class, VaadinIcon.TASKS.create()));
        DropTarget<SideNav> dt = DropTarget.create(taskNav);
        dt.addDropListener(this::handleDroppedItem);
        return taskNav;
    }

    @NotNull
    private static SideNav getDataNav() {
        var dataNav = new SideNav("Data");
        dataNav.setCollapsible(true);
        dataNav.addItem(new SideNavItem("Contacts", ContactList.class, VaadinIcon.USER.create()),
                new SideNavItem("Companies", CompanyList.class, VaadinIcon.FACTORY.create()),
                new SideNavItem("Leads", LeadList.class, VaadinIcon.STAR.create()),
                new SideNavItem("Skills", SkillList.class, VaadinIcon.TOOLS.create()),
                new SideNavItem("Consultants", ConsultantList.class, VaadinIcon.USER_CARD.create()));
        return dataNav;
    }

    @NotNull
    private static SideNav getArchiveNav() {
        var archiveNav = new SideNav("Archive");
        archiveNav.setCollapsible(true);
        archiveNav.setExpanded(false);
        archiveNav.addItem(new SideNavItem("Completed Assignments", CompletedAssignmentList.class, VaadinIcon.CLIPBOARD_USER.create()));
        archiveNav.addItem(new SideNavItem("Completed Tasks", CompletedTaskList.class, VaadinIcon.TASKS.create()));
        return archiveNav;
    }

    private SideNav getUserNav() {
        var userNav = new SideNav("User");
        userNav.setVisible(securityService.isAdmin());
        userNav.setCollapsible(true);
        userNav.setExpanded(false);
        userNav.addItem(new SideNavItem("User List", UserList.class, VaadinIcon.USERS.create()));
        return userNav;
    }

    private void handleDroppedItem(final DropEvent<SideNav> dropEvent) {
        dropEvent.getDragData().ifPresent(data -> {
            Object payload = ((Collection) data).iterator().next();
            if(payload instanceof Contact selectedContact) {
                taskForm.prepareForm(selectedContact, securityService.getAuthenticatedUser());

                taskDialog.open();
            }
        });
    }

    private void setupTaskDialog() {
        taskForm = new TaskForm();
        taskForm.addSaveListener(this::saveTask);
        taskForm.addCloseListener(e -> taskDialog.close());

        taskDialog.add(taskForm);
    }

    private void saveTask(final TaskForm.SaveEvent saveEvent) {
        crmService.saveTask(saveEvent.getTask());
        var notification = Notification.show("Task Created!", 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        taskDialog.close();
    }
}
