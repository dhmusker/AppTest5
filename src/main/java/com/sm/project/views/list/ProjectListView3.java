package com.sm.project.views.list;

import com.sm.project.data.entity.Project;
import com.sm.project.data.service.ProjectService;
import com.sm.project.views.MainView;
import com.sm.project.views.projectform.ProjectFormDialog2;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "project-list3", layout = MainView.class)
@PageTitle("Project List 3")
@Tag("project-list3")
public class ProjectListView3 extends VerticalLayout {

    @Id("projectlistgrid")
    private Grid<Project> grid;

//    private static String PROJECT_EDIT_ROUTE_TEMPLATE = "project-form-view/%d/edit";

    private ListDataProvider<Project> dataProvider;
    private ProjectService projectService;

    private HeaderRow headerRow;
    private HeaderRow filterRow;
    private Grid.Column<Project> idColumn;
    private Grid.Column<Project> titleColumn;
    private Grid.Column<Project> managerColumn;
    private Grid.Column<Project> dateStartedColumn;
    private Grid.Column<Project> dateEndedColumn;

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
    private Binder<Project> binder;


    // Constructor
    public ProjectListView3(@Autowired ProjectService projectService) {
        this.projectService = projectService;
        initGrid();
        initDataProvider();
        initGridColumns();
        initGridColumnFilters();
        this.add(grid);

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
            } else {
                UI.getCurrent().navigate(ProjectListView3.class);
            }
        });
    }

    // connect the data to the grid
    private void initDataProvider() {

        dataProvider = new ListDataProvider<Project>(projectService.findAll());
        grid.setDataProvider(dataProvider);

    }

    // Configure the table columns for the grid
    private void initGridColumns() {

        idColumn          = grid.addColumn(Project::getId, "id").setHeader("Id");
        titleColumn       = grid.addColumn(Project::getTitle, "title").setHeader("Title").setWidth("300px").setFlexGrow(1);
        managerColumn     = grid.addColumn(Project::getManager, "manager").setHeader("Manager").setFlexGrow(1);
        dateStartedColumn = grid.addColumn(Project::getDateStarted, "dateStarted").setHeader("Date Started").setFlexGrow(1);
        dateEndedColumn   = grid.addColumn(Project::getDateEnded, "dateEnded").setHeader("Date Ended").setFlexGrow(1);

        headerRow = grid.prependHeaderRow();
    }

    // Build the filters for the columns
    private void initGridColumnFilters() {

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
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getManager(), managerFilter.getValue())));
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
        dialog.setModal(false);
        dialog.open();
        VerticalLayout wrapper = new VerticalLayout();
        H3 heading = new H3("Project");

        FormLayout form = new FormLayout();
        id = new IntegerField("id");
        id.setReadOnly(true);
        id.setWidth("50px");
        title = new TextField("title");
        title.setWidth("50px");
        manager = new TextField("manager");
        manager.setWidth("50px");
        businessCase = new TextField("businessCase");
        dateStarted = new DatePicker("dateStarted");
        dateStarted.setWidth("50px");
        dateEnded = new DatePicker("dateEnded");
        dateEnded.setWidth("50px");
        isActive = new Checkbox("isActive");
        save = new Button("Save");
        save.setWidth("100px");
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

        cancel.addClickListener(e -> {
            clearForm();
            dialog.close();
            refreshGrid();
        });

        delete.addClickListener(e -> {
            projectService.delete(projectID);
            dialog.close();
            refreshGrid();
        });

        save.addClickListener(e -> {
            projectService.update(binder.getBean());
            dialog.close();
            grid.getDataProvider().refreshItem(binder.getBean());

        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setSpacing(true);
        buttons.add(save, delete, cancel);

        form.add(id, title, manager, businessCase, dateStarted, dateEnded, isActive );

        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        Span message = new Span();

        // Cancel action on ESC press
        Shortcuts.addShortcutListener(dialog, () -> {
            message.setText("Cancelled...");
            dialog.close();
        }, Key.ESCAPE);

        wrapper.add(heading, form, buttons, message);
        dialog.add(wrapper);
        dialog.setWidth("50%");

    }

    // Clear form action assigned to the Clear button
    private void clearForm() {

        binder.setBean(new Project());

    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

}