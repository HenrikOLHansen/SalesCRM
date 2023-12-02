package com.consid.application.views.components;

import com.consid.application.data.entity.Skill;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.List;

public class SkillsComponent extends CustomField<List<Skill>> {

    TextField filter = new TextField();
    MultiSelectListBox<Skill> skillList = new MultiSelectListBox<>();

    private List<Skill> allSkills;

    public SkillsComponent(List<Skill> selectedSkills, List<Skill> allSkills) {
        this.allSkills = allSkills;

        setLabel("Skills");

        filter.setPlaceholder("Filter by skill name...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateAllSkills());

        add(filter);

        skillList.setItems(allSkills);
        skillList.select(selectedSkills);
        skillList.setHeight("200px");

        add(skillList);
    }

    private void updateAllSkills() {
        List<Skill> filteredSkills = allSkills.stream()
                .filter(skill -> skill.getName().contains(filter.getValue()))
                .toList();

        skillList.setItems(filteredSkills);
    }

    @Override
    protected List<Skill> generateModelValue() {
        return new ArrayList<>(skillList.getSelectedItems());
    }

    @Override
    protected void setPresentationValue(List<Skill> skills) {
        allSkills = new ArrayList<>(skills);
    }
}
