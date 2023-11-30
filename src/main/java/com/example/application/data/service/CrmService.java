package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import com.example.application.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrmService {

    private final ContactRepository contactRepository;
    private final ConsidContactRepository considContactRepository;
    private final CompanyRepository companyRepository;
    private final LeadRepository leadRepository;
    private final SkillRepository skillRepository;
    private final ConsultantRepository consultantRepository;
    private final AssignmentRepository assignmentRepository;
    private final CompletedAssignmentRepository completedAssignmentRepository;
    private final TaskRepository taskRepository;
    private final CompletedTaskRepository completedTaskRepository;
    private final SecurityService securityService;


    public List<Contact> findAllContacts(final String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public List<Consultant> findConsultantsBySkill(final String skillFilter) {
        if(skillFilter == null || skillFilter.isEmpty()) {
            return consultantRepository.findAll();
        } else {
            return consultantRepository.search(skillFilter);
        }
    }

    public List<ConsidContact> findAllConsidContracts() { return considContactRepository.findAll(); }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(final Contact contact) {
        if (contact == null) {
            log.error("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    public void saveSkill(final Skill skill) {
        if (skill == null) {
            log.error("Skill is null. Are you sure you have connected your form to the application?");
            return;
        }
        skillRepository.save(skill);
    }

    public void saveConsultant(Consultant consultant) {
        if(consultant == null) {
            log.error("Consultant is null. Are you sure you have connected your form to the application?");
            return;
        }
        consultantRepository.save(consultant);
    }

    public void saveLead(Lead lead) {
        if(lead == null) {
            log.error("Lead is null. Are you sure you have connected your form to the application?");
            return;
        }
        leadRepository.save(lead);
    }

    public void saveAssignment(Assignment assignment) {
        if(assignment == null) {
            log.error("Assignment is null. Are you sure you have connected your form to the application?");
            return;
        }
        assignmentRepository.save(assignment);
    }

    public void archiveAssignment(Assignment assignment) {
        CompletedAssignment completedAssignment = new CompletedAssignment(assignment);
        completedAssignmentRepository.save(completedAssignment);
        assignmentRepository.delete(assignment);
    }

    public void archiveTask(CompletedTask completedTask, Task task) {
        completedTaskRepository.save(completedTask);
        taskRepository.delete(task);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTask(Task task) { taskRepository.delete(task); }

    public void deleteLead(Lead lead) {
        leadRepository.delete(lead);
    }

    public List<Company> findAllCompanies() { return companyRepository.findAll(); }

    public List<Lead> findAllLeads() { return leadRepository.findAll(); }

    public List<Skill> findAllSkills() { return skillRepository.findAll(); }

    public List<Consultant> findAllConsultants() { return consultantRepository.findAll(); }

    public List<Assignment> findAllAssignments() { return assignmentRepository.findAll(); }

    public List<Task> findAllTasks() {
        return taskRepository.findAllByUsernameOrderByDueDateAsc(getCurrentLoggedInUsername());
    }

    public List<CompletedTask> findAllCompletedTasks() {
        return completedTaskRepository.findAllByUsernameOrderByCompletionDateAsc(getCurrentLoggedInUsername());
    }

    public List<Task> findLastFiveTasks() { return findAllTasks().stream().limit(5).toList(); }

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    public void saveConsidContacts(List<String> considContacts) {
        for (String name : considContacts) {
            ConsidContact considContact = new ConsidContact();
            considContact.setFirstName(name);
            considContactRepository.save(considContact);
        }
    }

    public List<CompletedAssignment> findAllCompletedAssignments() {
        return completedAssignmentRepository.findAllByOrderByEndDateDesc();
    }

    private String getCurrentLoggedInUsername() {
        return securityService.getAuthenticatedUser().getUsername();
    }
}
