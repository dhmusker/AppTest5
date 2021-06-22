package com.sm.project.views.projectform;

import com.sm.project.data.entity.Project;
import com.sm.project.data.service.ProjectService;
import com.sm.project.views.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "project-dialog2-view", layout = MainView.class)
//
@PageTitle("Project Dialog 2")
@Tag("project-dialog2-view")
@JsModule("./views/project-dialog2-view.ts")
public class ProjectFormDialog2 extends LitTemplate {

    private final String PROJECT_LIST_ROUTE_TEMPLATE = "project-list3";
    public ProjectService service;
    public Integer projectId;

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
    public Button save;
    @Id("delete")
    public Button delete;
    @Id("cancel")
    public Button cancel;

    public Binder<Project> binder = new Binder<>(Project.class);

    // Form Constructor, bind the bean to the form, assign actions to the Save, Clear, Delete buttons
    public ProjectFormDialog2(ProjectService projectService) {


    }


    // Form Constructor, bind the bean to the form, assign actions to the Save, Clear, Delete buttons
    public ProjectFormDialog2(ProjectService projectService, Integer projectID) {

        System.out.println("Dialog opener: id = " + projectId);

        service = projectService;
        projectId = projectID;


    }

    // Clear form action assigned to the Clear button
    public void clearForm() {

        this.binder.setBean(new Project());

    }

}
