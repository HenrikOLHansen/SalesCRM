package com.example.application.views;

import com.example.application.views.list.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    
    public MainLayout() {
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
    }

    private VerticalLayout getSideNav() {
        SideNav topNav = new SideNav("Status");
        topNav.addItem(new SideNavItem("Status", StatusPage.class, VaadinIcon.BAR_CHART_V.create()),
                new SideNavItem("Assignments", AssignmentList.class, VaadinIcon.CLIPBOARD_USER.create()));

        SideNav taskNav = new SideNav("Tasks");
        // TODO add tasks (call list)

        SideNav dataNav = new SideNav("Data");
        dataNav.setCollapsible(true);
        dataNav.addItem(new SideNavItem("Contacts", ContactList.class, VaadinIcon.USER.create()),
                new SideNavItem("Companies", CompanyList.class, VaadinIcon.FACTORY.create()),
                new SideNavItem("Leads", LeadList.class, VaadinIcon.STAR.create()),
                new SideNavItem("Skills", SkillList.class, VaadinIcon.TOOLS.create()),
                new SideNavItem("Consultants", ConsultantList.class, VaadinIcon.USER_CARD.create()));

        SideNav archiveNav = new SideNav("Archive");
        archiveNav.setCollapsible(true);
        archiveNav.addItem(new SideNavItem("Completed Assignments", CompletedAssignmentList.class, VaadinIcon.ARCHIVE.create()));

        VerticalLayout navWrapper = new VerticalLayout(topNav, dataNav, archiveNav);
        navWrapper.setSpacing(true);
        navWrapper.setSizeUndefined();

        return navWrapper;
    }
}
