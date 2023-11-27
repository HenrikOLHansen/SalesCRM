package com.example.application.views;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.example.application.views.components.TaskForm;
import com.example.application.views.list.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Collection;

public class MainLayout extends AppLayout {

    private final CrmService crmService;

    TaskForm taskForm;
    Dialog taskDialog = new Dialog();

    public MainLayout(CrmService crmService) {
        this.crmService = crmService;
        createDrawer();
    }

    private void createDrawer() {
        H1 logo = new H1("Sales CRM");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToDrawer(getSideNav());
        addToNavbar(header);

        setupTaskDialog();
    }

    private VerticalLayout getSideNav() {
        SideNav topNav = new SideNav("Status");
        topNav.addItem(new SideNavItem("Status", StatusPage.class, VaadinIcon.BAR_CHART_V.create()),
                new SideNavItem("Assignments", AssignmentList.class, VaadinIcon.CLIPBOARD_USER.create()));

        SideNav taskNav = new SideNav("Tasks");
        taskNav.addItem(new SideNavItem("Task List", TaskList.class, VaadinIcon.TASKS.create()));
        DropTarget<SideNav> dt = DropTarget.create(taskNav);
        dt.addDropListener(this::handleDroppedItem);

        SideNav dataNav = new SideNav("Data");
        dataNav.setCollapsible(true);
        dataNav.addItem(new SideNavItem("Contacts", ContactList.class, VaadinIcon.USER.create()),
                new SideNavItem("Companies", CompanyList.class, VaadinIcon.FACTORY.create()),
                new SideNavItem("Leads", LeadList.class, VaadinIcon.STAR.create()),
                new SideNavItem("Skills", SkillList.class, VaadinIcon.TOOLS.create()),
                new SideNavItem("Consultants", ConsultantList.class, VaadinIcon.USER_CARD.create()));

        SideNav archiveNav = new SideNav("Archive");
        archiveNav.setCollapsible(true);
        archiveNav.setExpanded(false);
        archiveNav.addItem(new SideNavItem("Completed Assignments", CompletedAssignmentList.class, VaadinIcon.ARCHIVE.create()));

        VerticalLayout navWrapper = new VerticalLayout(topNav, dataNav, taskNav, archiveNav);
        navWrapper.setSpacing(true);
        navWrapper.setSizeUndefined();

        return navWrapper;
    }

    private void handleDroppedItem(DropEvent<SideNav> dropEvent) {
        dropEvent.getDragData().ifPresent(data -> {
            Object payload = ((Collection) data).iterator().next();
            if(payload instanceof Contact selectedContact) {
                taskForm.prepareForm(selectedContact);

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

    private void saveTask(TaskForm.SaveEvent saveEvent) {
        crmService.saveTask(saveEvent.getTask());
        Notification notification = Notification.show("Task Created!", 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        taskDialog.close();
    }
}
