package com.consid.application.views.list;

import com.consid.application.data.entity.Skill;
import com.consid.application.data.entity.SkillType;
import com.consid.application.data.service.CrmService;
import com.consid.application.views.MainLayout;
import com.consid.application.views.form.SkillForm;
import com.consid.application.views.form.SkillTypeForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Skills | Sales CRM")
@Route(value = "skills", layout = MainLayout.class)
public class SkillList extends HorizontalLayout {

    Grid<Skill> skillsGrid = new Grid<>(Skill.class, false);
    Grid<SkillType> skillTypesGrid = new Grid<>(SkillType.class, false);

    Dialog skillDialog = new Dialog();
    Dialog skillTypeDialog = new Dialog();
    SkillForm skillForm;
    SkillTypeForm skillTypeForm;
    private final CrmService crmService;

    public SkillList(CrmService crmService) {
        this.crmService = crmService;

        setSizeFull();

        add(createSkillsLayout(), createSkillTypesLayout());

        setupSkillDialog();
        setupSkillTypeDialog();

        updateGridContent();
    }

    private VerticalLayout createSkillsLayout() {
        var layout = new VerticalLayout();

        H2 header = new H2("Skills");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        layout.add(header);

        Button createNewSkillButton = new Button("New Skill");
        createNewSkillButton.addClickListener(this::createNewSkill);

        layout.add(createNewSkillButton, skillsGrid);

        configureSkillsGrid();

        return layout;
    }

    private VerticalLayout createSkillTypesLayout() {
        var layout = new VerticalLayout();

        H2 header = new H2("Skill Types");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        layout.add(header);

        configureSkillTypesGrid();

        Button createNewSkillTypeButton = new Button("New Skill Type");
        createNewSkillTypeButton.addClickListener(this::createNewSkillType);

        layout.add(createNewSkillTypeButton, skillTypesGrid);

        return layout;
    }

    private void createNewSkill(ClickEvent<Button> buttonClickEvent) {
        skillForm.setSkill(new Skill());
        skillDialog.open();
    }

    private void createNewSkillType(ClickEvent<Button> buttonClickEvent) {
        skillTypeForm.setSkillType(new SkillType());
        skillTypeDialog.open();
    }

    private void editSkill(Skill skill) {
        skillForm.setSkill(skill);
        skillDialog.open();
    }

    private void editSkillType(SkillType skillType) {
        skillTypeForm.setSkillType(skillType);
        skillTypeDialog.open();
    }

    private void setupSkillDialog() {
        skillForm = new SkillForm(crmService.findAllSkillTypes());
        skillForm.addSaveListener(this::saveSkill);
        skillForm.addCloseListener(e -> skillDialog.close());

        skillDialog.add(skillForm);
    }

    private void setupSkillTypeDialog() {
        skillTypeForm = new SkillTypeForm();
        skillTypeForm.addSaveListener(this::saveSkillType);
        skillTypeForm.addCloseListener(e -> skillTypeDialog.close());

        skillTypeDialog.add(skillTypeForm);
    }

    private void saveSkill(SkillForm.SaveEvent saveEvent) {
        crmService.saveSkill(saveEvent.getSkill());
        updateGridContent();
        skillDialog.close();
    }

    private void saveSkillType(SkillTypeForm.SaveEvent saveEvent) {
        crmService.saveSkillType(saveEvent.getSkillType());
        updateGridContent();
        skillTypeDialog.close();
    }

    private void configureSkillsGrid() {
        skillsGrid.addClassNames("skill-grid");
        skillsGrid.setSizeFull();
        skillsGrid.addColumn(Skill::getName).setHeader("Name");
        skillsGrid.addComponentColumn(this::skillTypeRenderer).setHeader("Skill Type");
        skillsGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        skillsGrid.asSingleSelect().addValueChangeListener(event -> editSkill(event.getValue()));
    }

    private Span skillTypeRenderer(Skill skill) {
        Span span = new Span(skill.getSkillType().getType());
        span.getElement().getThemeList().add("badge contrast");
        return span;
    }

    private void configureSkillTypesGrid() {
        skillTypesGrid.addClassNames("skill-types-grid");
        skillTypesGrid.setSizeFull();
        skillTypesGrid.addColumn(SkillType::getType);
        skillTypesGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        skillTypesGrid.asSingleSelect().addValueChangeListener(event -> editSkillType(event.getValue()));
    }

    private void updateGridContent() {
        var skills = crmService.findAllSkills();
        var skillTypes = crmService.findAllSkillTypes();

        skillForm.updateSkillTypes(skillTypes);
        skillsGrid.setItems(skills);
        skillTypesGrid.setItems(skillTypes);
    }
}
