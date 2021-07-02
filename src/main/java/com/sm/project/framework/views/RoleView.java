package com.sm.project.framework.views;

import com.sm.project.framework.data.entity.Permission;
import com.sm.project.framework.data.entity.Role;
import com.sm.project.framework.data.service.RoleService;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Route(value = "roles/:roleID?/:action?(edit)", layout = MainView.class)
@PageTitle("Roles")
public class RoleView extends Div implements BeforeEnterObserver {

    private final String ROLE_ID = "roleID";
    private final String ROLE_EDIT_ROUTE_TEMPLATE = "roles/%d/edit";
    private Integer orgId;
    private Integer userId;

    private IntegerField id;
    private IntegerField organisation;
    private TextField name;
    private TextField purpose;
    private IntegerField lastModifiedBy;
    private DateTimePicker lastModifiedDateTime;

    private Button cancelButton = new Button("Cancel");
    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button addButton = new Button("Add New Record");

    private BeanValidationBinder<Role> roleBinder;
    private Grid<Role> grid = new Grid<>(Role.class, false);
    private Role role;
    private RoleService roleService;

    public RoleView(@Autowired RoleService service) {

        // Prepare the business logic service
        this.roleService = service;

        createUI();

        // Configure Form
        roleBinder = new BeanValidationBinder<>(Role.class);

        // Bind fields. This where you'd define e.g. validation rules

        roleBinder.bindInstanceFields(this);

        cancelButton.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        deleteButton.addClickListener(e -> {
            roleService.delete(this.role.getId());
            clearForm();
            refreshGrid();
        });

        deleteButton.addClickListener(e -> {
            try {
                if (this.role == null) {
                    this.role = new Role();
                }
                roleBinder.writeBean(this.role);

                roleService.update(this.role);
                clearForm();
                refreshGrid();
                Notification.show("Role details stored.");
                UI.getCurrent().navigate(RoleView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Role details.");
            }
        });
    }

    private void createUI() {

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        // Name the classes in the view so they can be styled
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");

        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("organisation").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("purpose").setAutoWidth(true);
        grid.addColumn("lastModifiedBy").setAutoWidth(true);
        grid.addColumn("lastModifiedDateTime").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(roleService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ROLE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(RoleView.class);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> roleId = event.getRouteParameters().getInteger(ROLE_ID);
        if (roleId.isPresent()) {
            Optional<Role> roleFromBackend = roleService.get(roleId.get());
            if (roleFromBackend.isPresent()) {
                populateForm(roleFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested role was not found, ID = %d", roleId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(RoleView.class);
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
        id = new IntegerField("Id");
        id.setReadOnly(true);
        organisation = new IntegerField("Organisation ID");
        organisation.setReadOnly(true);
        name = new TextField("Role Name");
        purpose = new TextField("Purpose");
        lastModifiedBy = new IntegerField("Last Modified By");
        lastModifiedBy.setReadOnly(true);
        lastModifiedDateTime = new DateTimePicker("Last Modified Date");
        lastModifiedDateTime.setReadOnly(true);

        Component[] fields = new Component[]{id, organisation, name, purpose, lastModifiedBy, lastModifiedDateTime};

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

        addButton = new Button("Add new Role");
        addButton.addClickListener(e -> {
            clearForm();
            this.role = new Role( orgId,
                    "",
                    "",
                    userId,
                    LocalDateTime.now());
            roleBinder.readBean(this.role);
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

    private void populateForm(Role value) {
        this.role = value;
        roleBinder.readBean(this.role);

    }

}
