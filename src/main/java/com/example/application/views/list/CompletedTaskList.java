package com.example.application.views.list;

import com.example.application.data.entity.CompletedTask;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.stream.Stream;

@PageTitle("Completed Tasks | Sales CRM")
@Route(value = "completedTasks", layout = MainLayout.class)
public class CompletedTaskList extends VerticalLayout {

    Grid<CompletedTask> grid = new Grid<>(CompletedTask.class, false);

    private final CrmService crmService;

    public CompletedTaskList(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Assignments");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(grid);

        // TODO setupDialog();

        configureGrid();
        updateGridContent();
    }

    private void configureGrid() {
        grid.addClassName("completed-task-grid");
        grid.setSizeFull();
        grid.addColumn(task -> task.getContact().toString()).setHeader("Contact");
        grid.addColumn(CompletedTask::getCompletionDate).setHeader("Completion Date");
        grid.addColumn(CompletedTask::getDescription).setHeader("Description");
        grid.addColumn(CompletedTask::getNotes).setHeader("Notes");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItemDetailsRenderer(createTaskRenderer());
    }

    private static ComponentRenderer<TaskDetailsFormLayout, CompletedTask> createTaskRenderer() {
        return new ComponentRenderer<>(TaskDetailsFormLayout::new, TaskDetailsFormLayout::setTask);
    }

    private static class TaskDetailsFormLayout extends FormLayout {
        private final TextField contact = new TextField("Contact");
        private final DatePicker dueDate = new DatePicker("Due Date");
        private final DatePicker completedDate = new DatePicker("Completion Date");
        private final TextArea description = new TextArea("Description");
        private final TextArea notes = new TextArea("Notes");

        public TaskDetailsFormLayout() {
            setColspan(contact, 2);
            setColspan(description, 2);
            setColspan(notes, 2);

            Stream.<AbstractSinglePropertyField>of(contact, dueDate, completedDate, description, notes)
                    .forEach(field -> {
                        field.setReadOnly(true);
                        add(field);
                    });

            setResponsiveSteps(new ResponsiveStep("0", 2));
        }

        public void setTask(CompletedTask task) {
            contact.setValue(task.getContact().toString());
            dueDate.setValue(task.getDueDate());
            completedDate.setValue(task.getCompletionDate());
            description.setValue(task.getDescription());
            notes.setValue(task.getNotes());
        }
    }

    private void updateGridContent() { grid.setItems(crmService.findAllCompletedTasks()); }
}
