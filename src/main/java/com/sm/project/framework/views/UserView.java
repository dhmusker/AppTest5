package com.sm.project.framework.views;

import com.sm.project.framework.data.entity.Role;
import com.sm.project.framework.data.entity.User;
import com.sm.project.framework.data.service.UserService;
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

@Route(value = "users/:userID?/:action?(edit)", layout = MainView.class)
@PageTitle("Users")
public class UserView extends Div implements BeforeEnterObserver {

    private final String USER_ID = "userID";
    private final String USER_EDIT_ROUTE_TEMPLATE = "users/%d/edit";
    private Integer orgId;
    private Integer userId;

    private IntegerField id;
    private IntegerField organisation;
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private DatePicker dateEnrolled;
    private TextField title;
    private Checkbox userActive;
    private IntegerField lastModifiedBy;
    private DateTimePicker lastModifiedDateTime;

    private Button cancelButton = new Button("Cancel");
    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button addButton = new Button("Add New Record");

    private BeanValidationBinder<User> userBinder;
    private Grid<User> grid = new Grid<>(User.class, false);
    private User user;
    private UserService userService;


    public UserView(@Autowired UserService service) {

        // Name the classes in the view so they can be styled
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");

        // Prepare the business logic service
        this.userService = service;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("organisation").setAutoWidth(true);
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("title").setAutoWidth(true);
        TemplateRenderer<User> importantUserRenderer = TemplateRenderer.<User>of(
                "<iron-icon hidden='[[!item.userActive]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.userActive]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("userActive", User::isUserActive);

        grid.addColumn(importantUserRenderer).setHeader("User Active").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(userService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(USER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(UserView.class);
            }
        });

        // Configure Form
        userBinder = new BeanValidationBinder<>(User.class);

        // Bind fields. This where you'd define e.g. validation rules

        userBinder.bindInstanceFields(this);

        cancelButton.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        deleteButton.addClickListener(e -> {
            userService.delete(this.user.getId());
            clearForm();
            refreshGrid();
        });

        saveButton.addClickListener(e -> {
            try {
                if (this.user == null) {
                    this.user = new User();
                }
                userBinder.writeBean(this.user);

                userService.update(this.user);
                clearForm();
                refreshGrid();
                Notification.show("User details stored.");
                UI.getCurrent().navigate(UserView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the user details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> userIdToEdit = event.getRouteParameters().getInteger(USER_ID);
        if (userIdToEdit.isPresent()) {
            Optional<User> userFromBackend = userService.get(userIdToEdit.get());
            if (userFromBackend.isPresent()) {
                populateForm(userFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested user was not found, ID = %d", userIdToEdit.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(UserView.class);
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
        organisation = new IntegerField("Organisation ID");
        organisation.setReadOnly(true);
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        dateEnrolled = new DatePicker("Date Enrolled");
        title = new TextField("Title");
        userActive = new Checkbox("Active");
        userActive.getStyle().set("padding-top", "var(--lumo-space-m)");
        lastModifiedBy = new IntegerField("Last Modified By");
        lastModifiedBy.setReadOnly(true);
        lastModifiedDateTime = new DateTimePicker("Date Modified");
        lastModifiedDateTime.setReadOnly(true);

        Component[] fields = new Component[]{id, organisation, firstName, lastName, email, dateEnrolled, title, userActive, lastModifiedBy, lastModifiedDateTime};

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

        addButton = new Button("Add new User");
        addButton.addClickListener(e -> {
            clearForm();
            this.user = new User( orgId,
                    "",
                    "",
                    "",
                    LocalDate.now(),
                    "",
                    false,
                    userId,
                    LocalDateTime.now());
            userBinder.readBean(this.user);
            firstName.focus();
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

    private void populateForm(User value) {
        this.user = value;
        userBinder.readBean(this.user);

    }

}
