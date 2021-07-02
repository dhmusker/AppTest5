package com.sm.project.framework.views;

import com.sm.project.framework.data.entity.MasterReferenceData;
import com.sm.project.framework.data.entity.Organisation;
import com.sm.project.framework.data.service.OrganisationService;
import com.sm.project.views.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Route(value = "org/:orgID?/:action?(edit)", layout = MainView.class)
@PageTitle("Organisations")
public class OrganisationView extends Div implements BeforeEnterObserver {

    private final String ORG_ID = "orgID";
    private final String ORG_EDIT_ROUTE_TEMPLATE = "org/%d/edit";
    private Integer orgId;
    private Integer userId;

    private IntegerField id;
    private TextField name;
    private IntegerField ownerId;
    private IntegerField lastModifiedBy;
    private DateTimePicker lastModifiedDateTime;

    private Button cancelButton = new Button("Cancel");
    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button addButton = new Button("Add New Record");

    private BeanValidationBinder<Organisation> orgBinder;
    private Grid<Organisation> grid = new Grid<>(Organisation.class, false);
    private Organisation organisation;
    private OrganisationService organisationService;


    public OrganisationView(@Autowired OrganisationService service) {

        // Name the classes in the view so they can be styled
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");

        // Prepare the business logic service
        this.organisationService = service;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("ownerId").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(organisationService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ORG_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(OrganisationView.class);
            }
        });

        // Configure Form
        orgBinder = new BeanValidationBinder<>(Organisation.class);

        // Bind fields. This where you'd define e.g. validation rules
        orgBinder.bindInstanceFields(this);

        cancelButton.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        deleteButton.addClickListener(e -> {
            organisationService.delete(this.organisation.getId());
            clearForm();
            refreshGrid();
        });

        saveButton.addClickListener(e -> {
            try {
                if (this.organisation == null) {
                    this.organisation = new Organisation();
                }
                orgBinder.writeBean(this.organisation);

                organisationService.update(this.organisation);
                clearForm();
                refreshGrid();
                Notification.show("Organisation details stored.");
                UI.getCurrent().navigate(OrganisationView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the organisation details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> organisationId = event.getRouteParameters().getInteger(ORG_ID);
        if (organisationId.isPresent()) {
            Optional<Organisation> organisationFromBackend = organisationService.get(organisationId.get());
            if (organisationFromBackend.isPresent()) {
                populateForm(organisationFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested organisation was not found, ID = %d", organisationId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(OrganisationView.class);
            }
        }
        orgId = 6299;
        userId = 2546;
// Pull these from the session once security is enabled
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id = new IntegerField("ID");
        id.setReadOnly(true);
        name = new TextField("Organisation Name");
        ownerId = new IntegerField("Owner");
        lastModifiedBy = new IntegerField("Last Modified By");
        lastModifiedBy.setReadOnly(true);
        lastModifiedDateTime = new DateTimePicker("Date Modified");
        lastModifiedDateTime.setReadOnly(true);

        Component[] fields = new Component[]{id, name, ownerId, lastModifiedBy, lastModifiedDateTime};

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
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(saveButton, cancelButton, deleteButton);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();

        addButton = new Button("Add new Organisation");
        addButton.addClickListener(e -> {
            clearForm();
            this.organisation = new Organisation( "",
                    userId,
                    userId,
                    LocalDateTime.now());
            orgBinder.readBean(this.organisation);
            name.focus();
        });

        wrapper.add(addButton, grid);
        splitLayout.addToPrimary(wrapper);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Organisation value) {
        this.organisation = value;
        orgBinder.readBean(this.organisation);

    }

}
