package com.sm.project.views.projectform;

import com.sm.project.data.entity.Project;
import com.sm.project.data.service.ProjectService;
import com.sm.project.views.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "project-dialog1-view", layout = MainView.class)
//
@PageTitle("Project Dialog 1")
@Tag("project-dialog1-view")
@JsModule("./views/project-dialog1-view.ts")
public class ProjectFormDialog1 extends LitTemplate {

    private final String PROJECT_LIST_ROUTE_TEMPLATE = "project-list";
    ProjectService service;
    Integer projectId;

    @Id("id")
    private IntegerField id;
    @Id("title")
    private TextField title;
    @Id("manager")
    private TextField manager;
    @Id("businessCase")
    private TextField businessCase;
    @Id("dateStarted")
    private DatePicker dateStarted;
    @Id("dateEnded")
    private DatePicker dateEnded;
    @Id("isActive")
    private Checkbox isActive;
    @Id("save")
    private Button save;
    @Id("delete")
    private Button delete;
    @Id("cancel")
    private Button cancel;

    private Binder<Project> binder = new Binder<>(Project.class);

    // Form Constructor, bind the bean to the form, assign actions to the Save, Clear, Delete buttons
    public ProjectFormDialog1(ProjectService projectService) {


    }

    // Form Constructor, bind the bean to the form, assign actions to the Save, Clear, Delete buttons
    public ProjectFormDialog1(ProjectService projectService, Integer projectID) {

        System.out.println("Dialog opener: id = " + projectId);

        service = projectService;
        projectId = projectID;

        binder.bindInstanceFields(this);

        clearForm();

        binder.setBean(service.get(projectId).orElse(new Project()));

        cancel.addClickListener(e -> {
            clearForm();
            UI.getCurrent().navigate(PROJECT_LIST_ROUTE_TEMPLATE);
        });

        delete.addClickListener(e -> {
            service.delete(binder.getBean().getId());
            clearForm();
            UI.getCurrent().navigate(PROJECT_LIST_ROUTE_TEMPLATE);
        });

        save.addClickListener(e -> {
            service.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " stored.");
            clearForm();
            UI.getCurrent().navigate(PROJECT_LIST_ROUTE_TEMPLATE);
        });

    }

    // Clear form action assigned to the Clear button
    private void clearForm() {

        this.binder.setBean(new Project());

    }

}
