package com.sm.project.views;

import com.sm.project.data.entity.User;
import com.sm.project.data.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
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

import java.util.Optional;

@Route(value = "users/:userID?/:action?(edit)", layout = MainView.class)
@PageTitle("Users")
public class UserView extends Div implements BeforeEnterObserver {

    private final String USER_ID = "userID";
    private final String USER_EDIT_ROUTE_TEMPLATE = "users/%d/edit";

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private DatePicker dateEnrolled;
    private TextField title;
    private Checkbox userActive;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

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

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
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
        Optional<Integer> userId = event.getRouteParameters().getInteger(USER_ID);
        if (userId.isPresent()) {
            Optional<User> userFromBackend = userService.get(userId.get());
            if (userFromBackend.isPresent()) {
                populateForm(userFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested user was not found, ID = %d", userId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(UserView.class);
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
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        dateEnrolled = new DatePicker("Date Enrolled");
        title = new TextField("Title");
        userActive = new Checkbox("Active");
        userActive.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{firstName, lastName, email, dateEnrolled, title, userActive};

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

    private void populateForm(User value) {
        this.user = value;
        userBinder.readBean(this.user);

    }

}
