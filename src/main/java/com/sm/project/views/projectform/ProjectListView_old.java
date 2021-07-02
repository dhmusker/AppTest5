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
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

//@Route(value = "project-list", layout = MainView.class)
//@PageTitle("Project List")
//@Tag("project-list")
public class ProjectListView_old extends VerticalLayout {
/*

    Logger logger = LoggerFactory.getLogger(getClass());

    @Id("projectlistgrid")
    private Grid<Project> grid;

    private ListDataProvider<Project> dataProvider;
    private ProjectService projectService;

    private HeaderRow headerRow;
    private HeaderRow filterRow;


    @Id("id")
    private IntegerField id;
    @Id("title")
    private TextField title;
    @Id("manager")
    private TextField manager;
    @Id("businessCase")
    private TextArea businessCase;
    @Id("category")
    private TextField category;
    @Id("dateStarted")
    private DatePicker dateStarted;
    @Id("dateEnded")
    private DatePicker dateEnded;
    @Id("isActive")
    private Checkbox isActive;
    @Id("add")
    private Button add;
    @Id("save")
    private Button save;
    @Id("delete")
    private Button delete;
    @Id("cancel")
    private Button cancel;
    private Binder<Project> binder;

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
    @Id("taskAdd")
    private Button taskAdd;
    @Id("taskSave")
    private Button taskSave;
    @Id("taskDelete")
    private Button taskDelete;
    @Id("taskCancel")
    private Button taskCancel;
    private Binder<ProjectTask> taskBinder;


    // Constructor
    public ProjectListView_old(@Autowired ProjectService projectService, ProjectTaskService projectTaskService) {
        this.projectService = projectService;
        this.projectTaskService = projectTaskService;

        initGrid();
        initGridColumns();
        add = new Button("Add new Project");
        add.addClickListener(e -> {
            openDataEntryForm( 0 );
        });
        this.add(add, grid);
        this.setHorizontalComponentAlignment(Alignment.END, add);

    }

    // Initialise the grid
    private void initGrid() {
        grid = new Grid(Project.class, false);
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        // when a row is selected move to the data entry form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                openDataEntryForm( event.getValue().getId() );
            }
        });

        dataProvider = new ListDataProvider<Project>(projectService.findAll());

        grid.setDataProvider(dataProvider);

    }

    // Configure the table columns for the grid
    private void initGridColumns() {

        Grid.Column<Project> idColumn          = grid.addColumn(Project::getId, "id").setHeader("Id");
        Grid.Column<Project> titleColumn       = grid.addColumn(Project::getTitle, "title").setHeader("Title").setWidth("300px").setFlexGrow(1);
        Grid.Column<Project> managerColumn     = grid.addColumn(Project::getProjectManagerName, "projectManagerName").setHeader("Project Manager").setFlexGrow(1);
        Grid.Column<Project> dateStartedColumn = grid.addColumn(Project::getDateStarted, "dateStarted").setHeader("Date Started").setFlexGrow(1);
        Grid.Column<Project> dateEndedColumn   = grid.addColumn(Project::getDateEnded, "dateEnded").setHeader("Date Ended").setFlexGrow(1);
        Grid.Column<Project> categoryNameColumn    = grid.addColumn(Project::getCategoryName, "categoryName").setHeader("Category").setFlexGrow(1);

        headerRow = grid.prependHeaderRow();
        filterRow = grid.appendHeaderRow();

        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> dataProvider.addFilter(
                project -> StringUtils.containsIgnoreCase(Integer.toString(project.getId()), idFilter.getValue())));
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField titleFilter = new TextField();
        titleFilter.setPlaceholder("Filter");
        titleFilter.setClearButtonVisible(true);
        titleFilter.setWidth("100%");
        titleFilter.setValueChangeMode(ValueChangeMode.EAGER);
        titleFilter.addValueChangeListener(event -> dataProvider
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getTitle(), titleFilter.getValue())));
        filterRow.getCell(titleColumn).setComponent(titleFilter);

        TextField managerFilter = new TextField();
        managerFilter.setPlaceholder("Filter");
        managerFilter.setClearButtonVisible(true);
        managerFilter.setWidth("100%");
        managerFilter.setValueChangeMode(ValueChangeMode.EAGER);
        managerFilter.addValueChangeListener(event -> dataProvider
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getProjectManagerName(), managerFilter.getValue())));
        filterRow.getCell(managerColumn).setComponent(managerFilter);

        DatePicker dateStartFilter = new DatePicker();
        dateStartFilter.setPlaceholder("Filter");
        dateStartFilter.setClearButtonVisible(true);
        dateStartFilter.setWidth("100%");
        dateStartFilter.addValueChangeListener(event -> dataProvider.addFilter(project -> areDatesEqual(project.getDateStarted(), dateStartFilter)));
        filterRow.getCell(dateStartedColumn).setComponent(dateStartFilter);

        DatePicker dateEndedFilter = new DatePicker();
        dateEndedFilter.setPlaceholder("Filter");
        dateEndedFilter.setClearButtonVisible(true);
        dateEndedFilter.setWidth("100%");
        dateEndedFilter.addValueChangeListener(event -> dataProvider.addFilter(project -> areDatesEqual(project.getDateEnded(), dateEndedFilter)));
        filterRow.getCell(dateEndedColumn).setComponent(dateEndedFilter);

        TextField categoryFilter = new TextField();
        categoryFilter.setPlaceholder("Filter");
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.setWidth("100%");
        categoryFilter.setValueChangeMode(ValueChangeMode.EAGER);
        categoryFilter.addValueChangeListener(event -> dataProvider
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getCategory(), categoryFilter.getValue())));
        filterRow.getCell(categoryColumn).setComponent(categoryFilter);

    }

    // Used in the filters, to map dates
    private boolean areDatesEqual(LocalDate currentDate, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            return dateFilterValue.equals(currentDate);
        }
        return true;
    }

    // When a user clicks on a row in the grid, open a data entry form so they can edit the record
    private void openDataEntryForm( Integer projectID ) {

        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.open();
        VerticalLayout wrapper = new VerticalLayout();

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));
        id = new IntegerField("Project ID");
        id.setReadOnly(true);
        title = new TextField("Project Title");
        manager = new TextField("Project Manager");
        businessCase = new TextArea("Business Case");
        dateStarted = new DatePicker("Date Started");
        dateEnded = new DatePicker("Date Ended");
        isActive = new Checkbox("isActive");
        category = new TextField("Category");
        save = new Button("Save");
        save.setWidth("100px");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete = new Button("Delete");
        delete.setWidth("100px");
        cancel = new Button("Cancel");
        cancel.setWidth("100px");

        binder = new Binder<>(Project.class);
        binder.setBean(projectService.get(projectID).orElse(new Project()));
        binder.bind(id, "id");
        binder.bind(title, "title");
        binder.bind(manager, "manager");
        binder.bind(businessCase, "businessCase");
        binder.bind(dateStarted, "dateStarted");
        binder.bind(dateEnded, "dateEnded");
        binder.bind(isActive, "isActive");
        binder.bind(category, "category");

        cancel.addClickListener(e -> {
            clearForm();
            dialog.close();
        });

        delete.addClickListener(e -> {
            projectService.deleteById(projectID);
            grid.setItems(projectService.findAll());
            UI.getCurrent().getSession().access(() -> dataProvider.refreshAll());
            dialog.close();
        });

        save.addClickListener(e -> {
            projectService.update(binder.getBean());
            grid.setItems(projectService.findAll());
            UI.getCurrent().getSession().access(() -> dataProvider.refreshAll());
            dialog.close();
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setAlignItems(Alignment.STRETCH);
        H3 heading = new H3("Project: " + projectID);
        heading.setWidth("60%");
        buttons.add(heading, save, delete, cancel);

        form.add( title );
        form.setColspan(title, 3);
        form.add( manager, category );
        form.add( businessCase );
        form.setColspan(businessCase, 3);
        businessCase.setHeight("60px");
        form.add( dateStarted, dateEnded, isActive );

        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        Span message = new Span();

        // Cancel action on ESC press
        Shortcuts.addShortcutListener(dialog, () -> {
            message.setText("Cancelled...");
            dialog.close();
        }, Key.ESCAPE);

        initTaskGrid( projectID ) ;
        initTaskDataProvider(projectID);
        initTaskGridColumns();

        taskAdd = new Button("Add new Project Task");
        taskAdd.addClickListener(e -> {
            openTaskDataEntryForm( projectID, 0 );
        });

        wrapper.add(buttons, form, taskAdd, taskgrid, message);
        dialog.add(wrapper);
        dialog.setWidth("60%");

    }

    // Clear form action assigned to the Clear button
    private void clearForm() {

        binder.setBean(new Project());

    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void refreshGridRow() {
        grid.getDataProvider().refreshItem(binder.getBean());
    }


    // Initialise the Project task grid
    private void initTaskGrid( Integer projectId) {
        taskgrid = new Grid(ProjectTask.class, false);
        taskgrid.setSelectionMode(SelectionMode.SINGLE);
        taskgrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

        // when a row is selected move to the data entry form
        taskgrid.asSingleSelect().addValueChangeListener(event -> {
        if (event.getValue() != null) {
            openTaskDataEntryForm( projectId, event.getValue().getId() );
        }
    });
    }

    // connect the data to the Project Task grid
    private void initTaskDataProvider(Integer projectId) {
        taskDataProvider = new ListDataProvider<ProjectTask>(projectTaskService.findByProjectId(projectId));
        taskgrid.setDataProvider(taskDataProvider);
        taskDataProvider.refreshAll();
    }

    // Configure the table columns for the grid
    private void initTaskGridColumns() {

        taskIdColumn           = taskgrid.addColumn(ProjectTask::getId, "id").setWidth("70px").setHeader("Id");
        taskProjectIdColumn    = taskgrid.addColumn(ProjectTask::getProjectId, "projectId").setHeader("Project ID").setWidth("70px").setFlexGrow(1);
        taskNameColumn         = taskgrid.addColumn(ProjectTask::getName, "name").setHeader("Task Name").setWidth("50%").setFlexGrow(1);
        taskAssignedToColumn   = taskgrid.addColumn(ProjectTask::getAssignedTo, "assignedTo").setHeader("Assigned To").setFlexGrow(1);
        taskStartDateColumn    = taskgrid.addColumn(ProjectTask::getStartDate, "StartDate").setHeader("Start Date").setFlexGrow(1);
        taskCategoryColumn     = taskgrid.addColumn(ProjectTask::getCategory, "category").setHeader("Category").setFlexGrow(1);

        taskHeaderRow = taskgrid.prependHeaderRow();
    }

    // When a user clicks on a row in the grid, open a data entry form so they can edit the record
    private void openTaskDataEntryForm( Integer taskprojectid, Integer taskID ) {

        Dialog taskDialog = new Dialog();
        taskDialog.setModal(true);
        taskDialog.open();
        VerticalLayout taskWrapper = new VerticalLayout();

        FormLayout taskForm = new FormLayout();
        taskForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));
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

        taskBinder = new Binder<>(ProjectTask.class);
        taskBinder.setBean(projectTaskService.get(taskID).orElse(new ProjectTask()));
        taskBinder.getBean().setProjectId(taskprojectid);

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
            taskDialog.close();
        });

        taskDelete.addClickListener(e -> {
            projectTaskService.deleteById(taskBinder.getBean().getId());
            taskgrid.setItems(projectTaskService.findByProjectId(taskprojectid));
            taskDialog.close();
            UI.getCurrent().getSession().access(() -> taskDataProvider.refreshAll());
        });


        taskSave.addClickListener(e -> {
            projectTaskService.update(taskBinder.getBean());
            taskgrid.setItems(projectTaskService.findByProjectId(taskprojectid));
            taskDialog.close();
            UI.getCurrent().getSession().access(() -> taskDataProvider.refreshAll());
        });

        HorizontalLayout taskButtons = new HorizontalLayout();
        taskButtons.setWidthFull();
        taskButtons.setAlignItems(Alignment.STRETCH);
        H3 heading = new H3("Project: "+ taskprojectid + " Task: " + taskID );
        heading.setWidth("50%");
        heading.getElement().setAttribute("v-align","top");
        taskButtons.add(heading, taskSave, taskDelete, taskCancel);

        taskForm.add( taskName );
        taskForm.setColspan(taskName, 2);
        taskForm.add( taskAssignedTo );
        taskForm.add( taskDescription );
        taskForm.setColspan(taskDescription, 3);
        taskForm.add( taskStartDate, taskEndDate, taskResources );
        taskForm.add( taskStartTime, taskEndTime, taskMinsDuration );
        taskForm.add( taskCategory, taskEffort, taskCost );


        taskDialog.setCloseOnEsc(false);
        taskDialog.setCloseOnOutsideClick(false);

        // Cancel action on ESC press
        Shortcuts.addShortcutListener(taskDialog, () -> {
            taskDialog.close();
        }, Key.ESCAPE);

        initTaskGrid( taskprojectid );
        initTaskDataProvider(taskID);
        initTaskGridColumns();

        taskWrapper.add(taskButtons, taskForm);
        taskDialog.add(taskWrapper);
        taskDialog.setWidth("50%");

    }

    // Clear form action assigned to the Clear button
    private void clearTaskForm() {
        taskBinder.setBean(new ProjectTask());
    }

    private void refreshTaskGrid(Integer taskprojectid) {
        taskDataProvider = new ListDataProvider<ProjectTask>(projectTaskService.findByProjectId(taskprojectid));
        taskgrid.setDataProvider(taskDataProvider);
    }

    private void refreshTaskGridRow() {
        taskgrid.getDataProvider().refreshItem(taskBinder.getBean());
    }
*/

}

