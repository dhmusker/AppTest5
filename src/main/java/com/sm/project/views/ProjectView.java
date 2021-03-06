package com.sm.project.views;

import com.sm.project.data.entity.Project;
import com.sm.project.data.service.ProjectService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.*;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.time.LocalDate;
import java.util.Optional;

@Route(value = "projects/:projectID?/:action?(edit)", layout = MainView.class)
@PageTitle("Projects")
public class ProjectView extends Div implements BeforeEnterObserver {

    private final String PROJECT_ID = "projectID";
    private final String PROJECT_EDIT_ROUTE_TEMPLATE = "projects/%d/edit";

    private TextField title;
    private TextField manager;
    private TextField businessCase;
    private DatePicker dateStarted;
    private DatePicker dateEnded;
    private Checkbox isActive;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Project> binder;
    private Grid<Project> grid = new Grid<>(Project.class, false);
    private Project project;
    private ProjectService projectService;

    public ProjectView(@Autowired ProjectService service) {

        // Name the classes in the view so they can be styled
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");

        // Prepare the business logic service
        this.projectService = service;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("title").setAutoWidth(true);
        grid.addColumn("manager").setAutoWidth(true);
        grid.addColumn("dateStarted").setAutoWidth(true);
        TemplateRenderer<Project> importantProjectRenderer = TemplateRenderer.<Project>of(
                "<iron-icon hidden='[[!item.isActive]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.isActive]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("Is Active", Project::isActive);

        grid.addColumn(importantProjectRenderer).setHeader("Project Active").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(projectService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProjectView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Project.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.project == null) {
                    this.project = new Project();
                }
                binder.writeBean(this.project);

                projectService.update(this.project);
                clearForm();
                refreshGrid();
                Notification.show("Project details stored.");
                UI.getCurrent().navigate(ProjectView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Project details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> projectId = event.getRouteParameters().getInteger(PROJECT_ID);
        if (projectId.isPresent()) {
            Optional<Project> projectFromBackend = projectService.get(projectId.get());
            if (projectFromBackend.isPresent()) {
                populateForm(projectFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested project was not found, ID = %d", projectId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProjectView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        title = new TextField("Project Title");
        manager = new TextField("Project Manager");
        businessCase = new TextField("Business Case");
        dateStarted = new DatePicker("Date Started");
        dateEnded = new DatePicker("Date Ended");
        isActive = new Checkbox("Active");
        isActive.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{title, manager, businessCase, dateStarted, dateEnded, isActive};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Project value) {
        this.project = value;
        binder.readBean(this.project);

    }

}
