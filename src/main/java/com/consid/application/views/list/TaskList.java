package com.consid.application.views.list;

import com.consid.application.data.entity.Task;
import com.consid.application.data.service.CrmService;
import com.consid.application.security.SecurityService;
import com.consid.application.views.MainLayout;
import com.consid.application.views.form.CompleteTaskForm;
import com.consid.application.views.form.TaskForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
 import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Task List | Sales CRM")
@Route(value = "tasklist", layout = MainLayout.class)
public class TaskList extends VerticalLayout {

    Grid<Task> grid = new Grid<>(Task.class, false);
    TaskForm taskForm;
    Dialog taskDialog = new Dialog();
    CompleteTaskForm completeTaskForm;
    Dialog completeTaskDialog = new Dialog();

    private final CrmService crmService;
    private final SecurityService securityService;

    public TaskList(CrmService crmService, SecurityService securityService) {
        this.crmService = crmService;
        this.securityService = securityService;

        H2 header = new H2("Tasks");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(createToolbar(), grid);

        setupDialogs();

        configureGrid();
        updateGridContent();
    }

    private void setupDialogs() {
        taskForm = new TaskForm();
        taskForm.addSaveListener(this::saveTask);
        taskForm.addCloseListener(e -> taskDialog.close());
        taskDialog.add(taskForm);

        completeTaskForm = new CompleteTaskForm();
        completeTaskForm.addSaveListener(this::saveCompletedTask);
        completeTaskForm.addCloseListener(e -> completeTaskDialog.close());
        completeTaskDialog.add(completeTaskForm);
    }

    private void saveCompletedTask(CompleteTaskForm.SaveEvent saveEvent) {
        crmService.archiveTask(saveEvent.getCompletedTask(), saveEvent.getExistingTask());

        completeTaskDialog.close();

        Notification.show("Task completed and archived...", 3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        updateGridContent();
    }

    private void saveTask(TaskForm.SaveEvent saveEvent) {
        crmService.saveTask(saveEvent.getTask());
        updateGridContent();
        taskDialog.close();
    }

    private void configureGrid() {
        grid.addClassName("task-grid");
        grid.setSizeFull();
        grid.addColumn(new ComponentRenderer<>(Button::new, (button, task) -> {
            button.setText("Complete");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            button.addClickListener(e -> this.completeTask(task));
        })).setHeader("Complete Task");
        grid.addColumn(task -> task.getContact().toString()).setHeader("Contact");
        grid.addColumn(Task::getDueDate).setHeader("Due Date");
        grid.addComponentColumn(createLink()).setHeader("Job Link");
        grid.addColumn(Task::getDescription).setHeader("Description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addItemClickListener(event -> editTask(event.getItem()));
    }

    private void editTask(Task task) {
        taskForm.prepareForm(task, securityService.getAuthenticatedUser());
        taskDialog.open();
    }

    private void completeTask(Task task) {
        completeTaskForm.prepareForm(task);
        completeTaskDialog.open();
    }

    private static ValueProvider<Task, Anchor> createLink() {
        return task -> StringUtils.isBlank(task.getLink())
                ? null
                : new Anchor(task.getLink(), "Open link", AnchorTarget.BLANK);
    }

    private Component createToolbar() {
        Button createNewTaskButton = new Button("New Task");
        createNewTaskButton.addClickListener(this::createNewTask);
        return new HorizontalLayout(createNewTaskButton);
    }

    private void createNewTask(ClickEvent<Button> buttonClickEvent) {
        taskForm.prepareForm(crmService.findAllContacts(""), securityService.getAuthenticatedUser());
        taskDialog.open();
    }

    private void updateGridContent() { grid.setItems(crmService.findAllTasks()); }

}
