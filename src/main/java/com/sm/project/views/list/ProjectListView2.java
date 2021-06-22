package com.sm.project.views.list;

import com.sm.project.data.entity.Project;
import com.sm.project.data.service.ProjectService;
import com.sm.project.views.MainView;
import com.sm.project.views.projectform.ProjectFormDialog1;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "project-list2", layout = MainView.class)
@PageTitle("Project List 2")
@Tag("project-list2")
public class ProjectListView2 extends VerticalLayout {

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

    // Constructor
    public ProjectListView2(@Autowired ProjectService projectService) {
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
                UI.getCurrent().navigate(ProjectListView2.class);
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
    private void openDataEntryForm( Integer id ) {

        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.open();

        System.out.println("Id selected: " + id);

        ProjectFormDialog1 form = new ProjectFormDialog1(projectService, id );

        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        Span message = new Span();

        Button confirmButton = new Button("Confirm", event -> {
            message.setText("Confirmed!");
            dialog.close();
        });
        Button cancelButton = new Button("Cancel", event -> {
            message.setText("Cancelled...");
            dialog.close();
        });

        // Cancel action on ESC press
        Shortcuts.addShortcutListener(dialog, () -> {
            message.setText("Cancelled...");
            dialog.close();
        }, Key.ESCAPE);

        dialog.add(new Div( form, confirmButton, cancelButton));

    }


}