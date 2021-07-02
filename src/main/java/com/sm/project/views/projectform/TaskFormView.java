package com.sm.project.views.projectform;

import com.sm.project.data.entity.Project;
import com.sm.project.data.entity.ProjectTask;
import com.sm.project.data.service.ProjectService;
import com.sm.project.data.service.ProjectTaskService;
import com.sm.project.views.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "task-form-view/:projectID?/:taskID?/:action?(edit)", layout = MainView.class)
@PageTitle("Project Task Form View")
@Tag("task-form-view")
public class TaskFormView extends VerticalLayout implements BeforeEnterObserver {

    Logger logger = LoggerFactory.getLogger(getClass());
    private static String PROJECT_EDIT_ROUTE_TEMPLATE = "project-form-view/%d/edit";

    private ProjectService projectService;
    private ProjectTaskService projectTaskService;
    private Binder<ProjectTask> taskBinder;
    Integer taskID;
    Integer projectID;

    FormLayout taskForm;
    HorizontalLayout taskButtons;

    @Id("taskId")
    private IntegerField taskId;
    @Id("taskProjectId")
    private IntegerField taskProjectId;
    @Id("taskName")
    private TextField taskName;
    @Id("taskDescription")
    private TextArea taskDescription;
    @Id("taskAssignedTo")
    private TextField taskAssignedTo;
    @Id("taskStartDate")
    private DatePicker taskStartDate;
    @Id("taskEndDate")
    private DatePicker taskEndDate;
    @Id("taskStartTime")
    private DateTimePicker taskStartTime;
    @Id("taskEndTime")
    private DateTimePicker taskEndTime;
    @Id("taskStatus")
    private TextField taskStatus;
    @Id("taskCategory")
    private TextField taskCategory;
    @Id("taskMinsDuration")
    private NumberField taskMinsDuration;
    @Id("taskResources")
    private IntegerField taskResources;
    @Id("taskEffort")
    private NumberField taskEffort;
    @Id("taskCost")
    private NumberField taskCost;
    @Id("taskSave")
    private Button taskSave;
    @Id("taskDelete")
    private Button taskDelete;
    @Id("taskCancel")
    private Button taskCancel;


    // Constructor
    public TaskFormView(@Autowired ProjectService projectService, ProjectTaskService projectTaskService) {

        this.projectService = projectService;
        this.projectTaskService = projectTaskService;

        initialiseForm();

        VerticalLayout taskWrapper = new VerticalLayout();
        taskWrapper.add(taskButtons, taskForm);
        this.add(taskWrapper);

    }


    private void initialiseForm() {

        taskForm = new FormLayout();
        taskForm.setClassName("sm-form-view");
        taskForm.setWidth("1200px");

        taskId = new IntegerField("Task ID");
        taskId.setReadOnly(true);
        taskProjectId = new IntegerField("Project ID");
        taskProjectId.setReadOnly(true);
        taskName = new TextField("Task Name");
        taskDescription = new TextArea("Description");
        taskAssignedTo = new TextField("Assigned To");
        taskStartDate = new DatePicker("Date Started");
        taskEndDate = new DatePicker("Date Ended");
        taskStartTime = new DateTimePicker("Time Started");
        taskEndTime = new DateTimePicker("Time Ended");
        taskStatus = new TextField("Status");
        taskCategory = new TextField("Category");
        taskMinsDuration = new NumberField("Duration");
        taskMinsDuration.setHasControls(true);
        taskResources = new IntegerField("Resources");
        taskResources.setHasControls(true);
        taskEffort = new NumberField("Effort");
        taskEffort.setHasControls(true);
        taskCost = new NumberField("Cost");
        taskCost.setHasControls(true);

        taskSave = new Button("Save");
        taskSave.setWidth("100px");
        taskSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        taskDelete = new Button("Delete");
        taskDelete.setWidth("100px");
        taskCancel = new Button("Cancel");
        taskCancel.setWidth("100px");

        taskButtons = new HorizontalLayout();
        taskButtons.setWidthFull();
        taskButtons.setAlignItems(FlexComponent.Alignment.STRETCH);

    }

    // When we first open this form, get the ID of the record to present from the URL
    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        logger.info( "Initialising Project form with the following parameters: " + event.getRouteParameters());

        taskBinder = new Binder<>(ProjectTask.class);

        if (event.getRouteParameters() != null) {

            taskID = Integer.parseInt(event
                    .getRouteParameters()
                    .get("taskID")
                    .orElse(""));

            projectID = Integer.parseInt(event
                    .getRouteParameters()
                    .get("projectID")
                    .orElse(""));

            if (taskID != null && taskID != 0) {
                taskBinder.setBean(projectTaskService.get(taskID).orElse(new ProjectTask()));
            } else {
                ProjectTask newTask = new ProjectTask();
                newTask.applyDefaults( "Dermot Musker" );
                taskBinder.setBean( newTask );
            }
        }

        bindFormData();
        createForm();

        Div wrapper = new Div();
        wrapper.setClassName("sm-form-wrapper");
        wrapper.add(taskForm);

        add(wrapper);

        logger.info( "Task form data initialised. Project ID : " + projectID + " Task ID: " + taskID );

    }

    private void bindFormData() {

        taskBinder.bind(taskId, "id");
        taskBinder.bind(taskProjectId, "projectId");
        taskBinder.bind(taskName, "name");
        taskBinder.bind(taskDescription, "description");
        taskBinder.bind(taskCategory, "category");
        taskBinder.bind(taskAssignedTo, "assignedTo");
        taskBinder.bind(taskStartDate, "startDate");
        taskBinder.bind(taskEndDate, "endDate");
        taskBinder.bind(taskStartTime, "startTime");
        taskBinder.bind(taskEndTime, "endTime");
        taskBinder.bind(taskStatus, "status");
        taskBinder.bind(taskMinsDuration, "minsDuration");
        taskBinder.bind(taskResources, "numResources");
        taskBinder.bind(taskEffort, "minsEffort");
        taskBinder.bind(taskCost, "cost");

        taskCancel.addClickListener(e -> {
            logger.info( "Project Task form user clicked Cancel Button in ProjectID:" + projectID);
            String str = String.format(PROJECT_EDIT_ROUTE_TEMPLATE, projectID);
            UI.getCurrent().navigate( str );
        });

        taskDelete.addClickListener(e -> {
            logger.info( "Project Task form user clicked Delete Button in ProjectID:" + projectID + " TaskID:" + taskBinder.getBean().getId());
            projectTaskService.deleteById(taskBinder.getBean().getId());
            String str = String.format(PROJECT_EDIT_ROUTE_TEMPLATE, projectID);
            UI.getCurrent().navigate( str );
        });


        taskSave.addClickListener(e -> {
            logger.info( "Project Task form user clicked Save Button for ProjectID:" + projectID + " TaskID:" + taskBinder.getBean().getId());
            taskBinder.getBean().setProjectId(projectID);
            ProjectTask savedTask = projectTaskService.update(taskBinder.getBean());
            logger.info( "Project ID: " + savedTask.getProjectId() + " TaskID: " + savedTask.getId() + " saved." );
            String str = String.format(PROJECT_EDIT_ROUTE_TEMPLATE, projectID);
            UI.getCurrent().navigate( str );
        });

    }

    private void createForm() {

        taskForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        H3 heading = new H3("Project: " + projectID + " Task: " + taskID);
        heading.setWidth("50%");
        heading.getElement().setAttribute("v-align", "top");
        taskButtons.add(heading, taskSave, taskDelete, taskCancel);

        taskForm.add( taskButtons, 3 );

        taskForm.add(taskName, 3);
        taskForm.add(taskAssignedTo);
        taskForm.add(taskDescription, 3);
        taskForm.add(taskStartDate, taskEndDate, taskResources);
        taskForm.add(taskStartTime, taskEndTime, taskMinsDuration);
        taskForm.add(taskCategory, taskEffort, taskCost);


    }

}