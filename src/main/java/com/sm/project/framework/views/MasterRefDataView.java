package com.sm.project.framework.views;

import com.sm.project.framework.data.entity.MasterReferenceData;
import com.sm.project.framework.data.entity.User;
import com.sm.project.framework.data.service.MasterReferenceDataService;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

@Route(value = "mrd/:mrdID?/:action?(edit)", layout = MainView.class)
@PageTitle("MasterData")
public class MasterRefDataView extends Div implements BeforeEnterObserver {

    private final String MRD_ID = "mrdID";
    private final String MRD_EDIT_ROUTE_TEMPLATE = "mrd/%d/edit";
    private Integer orgId;
    private Integer userId;

    private IntegerField id;
    private IntegerField organisation;
    private TextField dataset;
    private TextField refCode;
    private TextField refValue;
    private Checkbox isActive;
    private DatePicker activeFromDate;
    private DatePicker activeToDate;
    private IntegerField lastModifiedBy;
    private DateTimePicker lastModifiedDateTime;

    private Button cancelButton = new Button("Cancel");
    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button addButton = new Button("Add New Record");

    private BeanValidationBinder<MasterReferenceData> mrdBinder;
    private Grid<MasterReferenceData> grid = new Grid<>(MasterReferenceData.class, false);
    private MasterReferenceData masterReferenceData;
    private MasterReferenceDataService masterReferenceDataService;


    public MasterRefDataView(@Autowired MasterReferenceDataService service) {

        // Name the classes in the view so they can be styled
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");

        // Prepare the business logic service
        this.masterReferenceDataService = service;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("organisation").setAutoWidth(true);
        grid.addColumn("dataset").setAutoWidth(true);
        grid.addColumn("refCode").setAutoWidth(true);
        grid.addColumn("refValue").setAutoWidth(true);
        TemplateRenderer<MasterReferenceData> isActiveRenderer = TemplateRenderer.<MasterReferenceData>of(
                "<iron-icon hidden='[[!item.isActive]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.isActive]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("isActive", MasterReferenceData::getActive);

        grid.addColumn(isActiveRenderer).setHeader("Is Active").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(masterReferenceDataService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MRD_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterRefDataView.class);
            }
        });

        // Configure Form
        mrdBinder = new BeanValidationBinder<>(MasterReferenceData.class);

        // Bind fields. This where you'd define e.g. validation rules
        mrdBinder.bindInstanceFields(this);

        cancelButton.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        deleteButton.addClickListener(e -> {
            masterReferenceDataService.deleteById( orgId, this.masterReferenceData.getId());
            clearForm();
            refreshGrid();
        });

        saveButton.addClickListener(e -> {
            try {
                if (this.masterReferenceData == null) {
                    this.masterReferenceData = new MasterReferenceData();
                }
                mrdBinder.writeBean(this.masterReferenceData);

                masterReferenceDataService.update( this.masterReferenceData);
                clearForm();
                refreshGrid();
                Notification.show("MasterReferenceData details stored.");
                UI.getCurrent().navigate(MasterRefDataView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the masterReferenceData details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> masterReferenceDataId = event.getRouteParameters().getInteger(MRD_ID);
        if (masterReferenceDataId.isPresent()) {
            Optional<MasterReferenceData> masterReferenceDataFromBackend = masterReferenceDataService.get(masterReferenceDataId.get());
            if (masterReferenceDataFromBackend.isPresent()) {
                populateForm(masterReferenceDataFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested masterReferenceData was not found, ID = %d", masterReferenceDataId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterRefDataView.class);
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
        dataset = new TextField("Data Type");
        refCode = new TextField("Code");
        refValue = new TextField("Value");
        activeFromDate = new DatePicker("From Date");
        activeToDate = new DatePicker("To Date");
        lastModifiedBy = new IntegerField("Last Modified By");
        lastModifiedBy.setReadOnly(true);
        lastModifiedDateTime = new DateTimePicker("Date Modified");
        lastModifiedDateTime.setReadOnly(true);

        Component[] fields = new Component[]{id, organisation, dataset, refCode, refValue, activeFromDate, activeToDate, lastModifiedBy, lastModifiedDateTime};

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
        addButton = new Button("Add new Reference Data");
        addButton.addClickListener(e -> {
                clearForm();
                this.masterReferenceData = new MasterReferenceData( orgId,
                    "",
                    "",
                    "",
                    true,
                    LocalDate.now(),
                    null,
                    userId,
                    LocalDateTime.now());
            mrdBinder.readBean(this.masterReferenceData);
            dataset.focus();
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

    private void populateForm(MasterReferenceData value) {
        this.masterReferenceData = value;
        mrdBinder.readBean(this.masterReferenceData);

    }

}
