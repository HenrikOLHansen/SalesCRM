package com.consid.application.views.list;

import com.consid.application.data.entity.Skill;
import com.consid.application.data.service.CrmService;
import com.consid.application.views.MainLayout;
import com.consid.application.views.form.SkillForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Skills | Sales CRM")
@Route(value = "skills", layout = MainLayout.class)
public class SkillList extends VerticalLayout {

    Grid<Skill> grid = new Grid<>(Skill.class, false);

    Dialog skillDialog = new Dialog();
    SkillForm skillForm;
    private final CrmService crmService;

    public SkillList(CrmService crmService) {
        this.crmService = crmService;

        H2 header = new H2("Skills");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(createToolbar(), grid);

        setupDialog();

        configureGrid();
        updateGridContent();
    }

    private Component createToolbar() {
        Button createNewSkillButton = new Button("New Skill");
        createNewSkillButton.addClickListener(this::createNewSkill);
        return new HorizontalLayout(createNewSkillButton);
    }

    private void createNewSkill(ClickEvent<Button> buttonClickEvent) {
        skillForm.setSkill(new Skill());
        skillDialog.open();
    }

    private void editSkill(Skill skill) {
        skillForm.setSkill(skill);
        skillDialog.open();
    }

    private void setupDialog() {
        skillForm = new SkillForm();
        skillForm.addSaveListener(this::saveSkill);
        skillForm.addCloseListener(e -> closeDialog());

        skillDialog.add(skillForm);
    }

    private void saveSkill(SkillForm.SaveEvent saveEvent) {
        crmService.saveSkill(saveEvent.getSkill());
        updateGridContent();
        skillDialog.close();
    }

    private void closeDialog() { skillDialog.close(); }


    private void configureGrid() {
        grid.addClassNames("skill-grid");
        grid.setSizeFull();
        grid.addColumn("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateGridContent() {
        grid.setItems(crmService.findAllSkills());
    }
}
