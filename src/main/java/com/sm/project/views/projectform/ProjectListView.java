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
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "project-list-view", layout = MainView.class)
@PageTitle("Project List")
@Tag("project-list-view")
public class ProjectListView extends VerticalLayout {

    Logger logger = LoggerFactory.getLogger(getClass());
    private static String PROJECT_EDIT_ROUTE_TEMPLATE = "project-form-view/%d/edit";

    @Id("projectlistgrid")
    private Grid<Project> grid;

    private ListDataProvider<Project> dataProvider;
    private ProjectService projectService;

    private HeaderRow headerRow;
    private HeaderRow filterRow;

    @Id("add")
    private Button add;



    // Constructor
    public ProjectListView(@Autowired ProjectService projectService) {
        this.projectService = projectService;

        initGrid();
        initGridColumns();
        add = new Button("Add new Project");
        add.addClickListener(e -> {
            String str = String.format(PROJECT_EDIT_ROUTE_TEMPLATE, 0 );
            UI.getCurrent().navigate( str );
        });
        this.add(add, grid);
        this.setHorizontalComponentAlignment(FlexComponent.Alignment.END, add);

    }

    // Initialise the grid
    private void initGrid() {
        grid = new Grid(Project.class, false);
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        grid.asSingleSelect().addValueChangeListener(event -> {
            String str = String.format(PROJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId());
            logger.info( str );
            if (event.getValue() != null) {
                UI.getCurrent().navigate( str );
            } else {
                UI.getCurrent().navigate(ProjectListView.class);
            }
        });

        dataProvider = new ListDataProvider<Project>(projectService.findAll(6299));

        grid.setDataProvider(dataProvider);

    }

    // Configure the table columns for the grid
    private void initGridColumns() {

        Grid.Column<Project> idColumn              = grid.addColumn(Project::getId, "id").setHeader("Id");
        Grid.Column<Project> titleColumn           = grid.addColumn(Project::getTitle, "title").setHeader("Title").setWidth("300px").setFlexGrow(1);
        Grid.Column<Project> projectManagerColumn  = grid.addColumn(Project::getProjectManagerName, "projectManager").setHeader("Project Manager").setFlexGrow(1);
        Grid.Column<Project> dateStartedColumn     = grid.addColumn(Project::getDateStarted, "dateStarted").setHeader("Date Started").setFlexGrow(1);
        Grid.Column<Project> dateEndedColumn       = grid.addColumn(Project::getDateEnded, "dateEnded").setHeader("Date Ended").setFlexGrow(1);
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
        filterRow.getCell(projectManagerColumn).setComponent(managerFilter);

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

        TextField categoryNameFilter = new TextField();
        categoryNameFilter.setPlaceholder("Filter");
        categoryNameFilter.setClearButtonVisible(true);
        categoryNameFilter.setWidth("100%");
        categoryNameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        categoryNameFilter.addValueChangeListener(event -> dataProvider
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getCategoryName(), categoryNameFilter.getValue())));
        filterRow.getCell(categoryNameColumn).setComponent(categoryNameFilter);

    }

    // Used in the filters, to map dates
    private boolean areDatesEqual(LocalDate currentDate, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            return dateFilterValue.equals(currentDate);
        }
        return true;
    }

    private void refreshGrid() {
//        grid.select(null);
//        grid.getDataProvider().refreshAll();
    }

    private void refreshGridRow() {
//        grid.getDataProvider().refreshItem(binder.getBean());
    }



    // When a user clicks on a row in the grid, open a data entry form so they can edit the record
    private void openTaskDataEntryForm( Integer taskprojectid, Integer taskID ) {


    }


}

