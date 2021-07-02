package com.sm.project.views.projectform;

import com.sm.project.data.entity.Project;
import com.sm.project.data.entity.ProjectTask;
import com.sm.project.data.service.ProjectService;
import com.sm.project.data.service.ProjectTaskService;
import com.sm.project.framework.data.entity.MasterReferenceData;
import com.sm.project.framework.data.entity.User;
import com.sm.project.framework.data.service.MasterReferenceDataService;
import com.sm.project.framework.data.service.UserService;
import com.sm.project.views.CustomVerticalLayout;
import com.sm.project.views.MainView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Route(value = "project-form-view/:projectID?/:action?(edit)", layout = MainView.class)
@PageTitle("Project Form View")
@Tag("project-form-view")
public class ProjectFormView extends CustomVerticalLayout {

    public Logger logger = LoggerFactory.getLogger(getClass());
    private static String PROJECT_TASK_EDIT_ROUTE_TEMPLATE = "task-form-view/%d/%d/edit";
    private static Class PARENT_PAGE_ROUTE = ProjectListView.class;

    private ProjectService projectService;
    private UserService userService;
    private MasterReferenceDataService mrdService;
    private Binder<Project> binder = new Binder<>(Project.class);
    private Integer projectId;
    private Integer orgId = 6299;
// Needs to be set based on the user session
    private FormLayout form;
    private boolean hasChanged;

    @Id("id")
    private IntegerField id;
    private IntegerField organisation;
    @Id("title")
    private TextField title;
    @Id("manager")
    private ComboBox<User> userComboBox;
    private IntegerField projectManagerID;
    private TextField projectManagerName;
    @Id("businessCase")
    private TextArea businessCase;
    @Id("categoryName")
    private TextField categoryName;
    private ComboBox<MasterReferenceData> categoryComboBox;
    private TextField categoryCode;
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

    @Id("taskAdd")
    private Button taskAdd;

    HorizontalLayout buttons;
    Label heading;

    @Id("projecttaskgrid")
    private Grid<ProjectTask> taskgrid;
    private ListDataProvider<ProjectTask> taskDataProvider;
    private ProjectTaskService projectTaskService;
    private HeaderRow taskHeaderRow;
    private HeaderRow taskFilterRow;
    private Grid.Column<ProjectTask> taskIdColumn;
    private Grid.Column<ProjectTask> taskProjectIdColumn;
    private Grid.Column<ProjectTask> taskNameColumn;
    private Grid.Column<ProjectTask> taskAssignedToColumn;
    private Grid.Column<ProjectTask> taskStartDateColumn;
    private Grid.Column<ProjectTask> taskCategoryColumn;

    // Constructor
    public ProjectFormView(@Autowired ProjectService projectService, ProjectTaskService projectTaskService, UserService userService, MasterReferenceDataService mrdService) {

        this.projectService = projectService;
        this.projectTaskService = projectTaskService;
        this.mrdService = mrdService;
        this.userService = userService;

        initialiseForm();

    }

    private void initialiseForm() {

        form = new FormLayout();
        form.setClassName("sm-form-view");
        form.setWidth("1200px");

        id = new IntegerField("Project ID");
        id.setReadOnly(true);
        organisation = new IntegerField("Organisation");
        organisation.setReadOnly(true);
        title = new TextField("Project Title");

        projectManagerID = new IntegerField("Project Manager ID");
        projectManagerID.setReadOnly(true);
        projectManagerName = new TextField("Project Manager Name");

        userComboBox = new ComboBox<>();
        userComboBox.setAllowCustomValue(false);
        List<User> userList = userService.findAll( orgId );
        userComboBox.setItemLabelGenerator(User::getName);
        userComboBox.setItems(userList);

        userComboBox.addValueChangeListener((HasValue.ValueChangeEvent<User> event) -> {
            projectManagerID.setValue(event.getValue().getId());
            projectManagerName.setValue(event.getValue().getName());
        });

        businessCase = new TextArea("Business Case");
        dateStarted = new DatePicker("Date Started");
        dateEnded = new DatePicker("Date Ended");
        isActive = new Checkbox("isActive");
        categoryCode = new TextField("Category Code");
        categoryCode.setReadOnly(true);
        categoryName = new TextField("Category Name");

        categoryComboBox = new ComboBox<>();
        categoryComboBox.setAllowCustomValue(false);
        List<MasterReferenceData> mrdList = mrdService.getByOrgDataset( orgId, "Category" );
        logger.info("ProjectFormView: Category MRD Combo box has been populated with: " + mrdList.size() + " rows");
// Need to update the code once the Organisation is stored in the session for the user
// Also need to handle where no categories are set up for an organisation
        categoryComboBox.setItemLabelGenerator(MasterReferenceData::getRefValue);
        categoryComboBox.setItems(mrdList);

        categoryComboBox.addValueChangeListener((HasValue.ValueChangeEvent<MasterReferenceData> event) -> {
            if (event !=null && event.getValue() != null) {
                categoryCode.setValue(event.getValue().getRefCode());
                categoryName.setValue(event.getValue().getRefValue());
            }
        });

        save = new Button("Save");
        save.setWidth("100px");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete = new Button("Delete");
        delete.setWidth("100px");
        cancel = new Button("Cancel");
        cancel.setWidth("100px");

    }

    // When we first open this form, get the ID of the record to present from the URL
    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        logger.info( "Initialising Project form with the following parameters: " + event.getRouteParameters());

        binder = new Binder<>(Project.class);
        orgId = 6299;
// need to update this to reference the Organisation from the user session

        if (event.getRouteParameters() != null) {
            projectId = Integer.parseInt(event
                    .getRouteParameters()
                    .get("projectID")
                    .orElse("0"));

            if (projectId != null && projectId != 0) {
                binder.setBean(projectService.get(projectId).orElse(new Project()));
            } else {
// Need to update this with the actual user details that should be stored in the session
                binder.setBean( new Project( orgId, 2547, "Dermot Musker" ) );
            }

        }

        bindFormData();
        addChangeListeners();
        createForm();
        add( wrapForm( form ) );

        logger.info( "Project form data initialised. Project ID : " +binder.getBean().getId() );

    }

    private void bindFormData() {
        binder.bind(id, "id");
        binder.bind(organisation, "organisation");
        binder.bind(title, "title");
        binder.bind(projectManagerID, "projectManagerID");
        binder.bind(projectManagerName, "projectManagerName");
        binder.bind(businessCase, "businessCase");
        binder.bind(dateStarted, "dateStarted");
        binder.bind(dateEnded, "dateEnded");
        binder.bind(isActive, "isActive");
        binder.bind(categoryCode, "categoryCode");
        binder.bind(categoryName, "categoryName");

        cancel.addClickListener(e -> {
            logger.info( "Cancel button was clicked. Form hasChanged: " + hasChanged);

            if ( hasChanged ) {
                Dialog confirmChanges = new Dialog();
                confirmChanges.add(new Label("There are unsaved changes in this form, are you sure you want to move away without saving. Your changes will be lost."));
                Button returnbutton = new Button("Return To Form");
                Button discardbutton = new Button("Discard Changes");
                returnbutton.addClickListener(ex -> {
                    confirmChanges.close();
                });
                discardbutton.addClickListener(ex -> {
                    confirmChanges.close();
                    navigateToParentView( PARENT_PAGE_ROUTE );
                });
                confirmChanges.add(returnbutton, discardbutton);
                confirmChanges.open();
            } else {
                navigateToParentView( PARENT_PAGE_ROUTE );
            }
        });

        delete.addClickListener(e -> {
            projectService.deleteByProjectId( orgId, projectId );
            navigateToParentView( PARENT_PAGE_ROUTE );
        });

        save.addClickListener(e -> {
            projectService.update(binder.getBean());
            navigateToParentView( PARENT_PAGE_ROUTE );
        });

        if (binder.getBean() != null && binder.getBean().getProjectManagerID() != null) {
            Optional<User> pm = userService.get(binder.getBean().getProjectManagerID());
            if (!pm.isEmpty() && pm.get().getName() != null ) {
                userComboBox.setValue(pm.get());
            }
        }
        if (binder.getBean() != null && binder.getBean().getCategoryCode() != null) {
            List<MasterReferenceData> cat = mrdService.getByOrgDatasetCode( orgId, "Category", binder.getBean().getCategoryCode());
            if (!cat.isEmpty() && cat.get(0).getRefCode() != null ) {
                categoryComboBox.setValue(cat.get(0));
            }
        }

    }

    // Listen for any changes to the form, if the user then clicks the Cancel Button challenge them to save their changes
    private void addChangeListeners() {

        hasChanged = false;

        title.addValueChangeListener(event -> {                hasChanged = true; });
        projectManagerName.addValueChangeListener(event -> {   hasChanged = true; });
        projectManagerID.addValueChangeListener(event -> {     hasChanged = true; });
        businessCase.addValueChangeListener(event -> {         hasChanged = true; });
        dateStarted.addValueChangeListener(event -> {          hasChanged = true; });
        dateEnded.addValueChangeListener(event -> {            hasChanged = true; });
        isActive.addValueChangeListener(event -> {             hasChanged = true; });
        categoryCode.addValueChangeListener(event -> {         hasChanged = true; });
        categoryName.addValueChangeListener(event -> {         hasChanged = true; });

    }

    private void createForm() {

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        Div headingDiv = new Div();
        headingDiv.setClassName("sm-form-header-bar");
        heading = new Label("Project: " + projectId );
        heading.setWidth("60%");

        buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setAlignItems(FlexComponent.Alignment.STRETCH);

        buttons.add(heading, save, delete, cancel);
        headingDiv.add(buttons);

        form.add( headingDiv, 3 );
        form.add( title, 3 );

        Div userComboWrapper = new Div();
        userComboWrapper.add(projectManagerID);
        projectManagerID.setWidth("100px");
        userComboWrapper.add(userComboBox);

        Div categoryComboWrapper = new Div();
        categoryComboWrapper.add(categoryCode);
        categoryCode.setWidth("100px");
        categoryComboWrapper.add(categoryComboBox);

        form.add( userComboWrapper, categoryComboWrapper );

        form.add( businessCase, 3 );
        businessCase.setHeight("240px");
        form.add( dateStarted, dateEnded, isActive );

        initTaskGrid() ;
        initTaskDataProvider();
        initTaskGridColumns();

        taskAdd = new Button("Add new Project Task");
        taskAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        taskAdd.addClickListener(e -> {
            String str = String.format(PROJECT_TASK_EDIT_ROUTE_TEMPLATE, projectId, 0);
            logger.info( "Add Task row clicked. Project ID: " + str );
            if (binder.getBean().getId() != null) {
                UI.getCurrent().navigate( str );
            } else {
                navigateToParentView( PARENT_PAGE_ROUTE );
            }
        });

        Div taskAddButtonWrapper = new Div();
        taskAddButtonWrapper.add(taskAdd);
        Div taskGridWrapper = new Div();
        taskGridWrapper.add(taskgrid);
        form.add(taskAddButtonWrapper, 3);
        form.add(taskGridWrapper, 3);

    }

    // Initialise the Project task grid
    private void initTaskGrid() {

        taskgrid = new Grid(ProjectTask.class, false);
        taskgrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taskgrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

        // when a row is selected move to the data entry form
        taskgrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                String str = String.format(PROJECT_TASK_EDIT_ROUTE_TEMPLATE, projectId, event.getValue().getId());
                if ( projectId != null ) {
                    UI.getCurrent().navigate( str );
                } else {
                    navigateToParentView( PARENT_PAGE_ROUTE );
                }
            }
        });


    }

    // connect the data to the Project Task grid
    private void initTaskDataProvider() {

        taskDataProvider = new ListDataProvider<ProjectTask>(projectTaskService.findByProjectId(projectId));
        taskgrid.setDataProvider(taskDataProvider);
        taskDataProvider.refreshAll();

    }

    // Configure the table columns for the grid
    private void initTaskGridColumns() {

        taskIdColumn           = taskgrid.addColumn(ProjectTask::getId, "id").setHeader("Id").setFlexGrow(0);
        taskProjectIdColumn    = taskgrid.addColumn(ProjectTask::getProjectId, "projectId").setHeader("Project ID").setFlexGrow(0);
        taskNameColumn         = taskgrid.addColumn(ProjectTask::getName, "name").setHeader("Task Name").setWidth("300px").setFlexGrow(1);
        taskAssignedToColumn   = taskgrid.addColumn(ProjectTask::getAssignedTo, "assignedTo").setHeader("Assigned To").setFlexGrow(1);
        taskStartDateColumn    = taskgrid.addColumn(ProjectTask::getStartDate, "StartDate").setHeader("Start Date").setFlexGrow(1);
        taskCategoryColumn     = taskgrid.addColumn(ProjectTask::getCategory, "category").setHeader("Category").setFlexGrow(1);

        taskHeaderRow = taskgrid.prependHeaderRow();
    }

}
