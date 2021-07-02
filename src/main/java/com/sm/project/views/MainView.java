package com.sm.project.views;

import com.helger.commons.collection.impl.ICommonsNavigableMap;
import com.sm.project.framework.data.entity.MasterReferenceData;
import com.sm.project.framework.views.*;
import com.sm.project.views.projectform.ProjectListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Optional;

@Route(value="")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@StyleSheet("styles/styles.css")
public class MainView extends AppLayout {

    private final Tabs menu;
    private Label viewTitle;

    public MainView(){
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));

    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setClassName("sidemenu-header");
        layout.setWidth("100%");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getElement().setAttribute("theme", Lumo.DARK);
        layout.add(new DrawerToggle());
        viewTitle = new Label( "Project Management" );
        viewTitle.setClassName("sidemenu-header-text");
        viewTitle.setWidth("85%");
        layout.add(viewTitle);
        layout.add(new Avatar());
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().setAttribute("theme", Lumo.DARK);
        layout.setClassName("sidemenu-menu");
        layout.setHeight("95%");
        layout.setWidth("235px");
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
//        logoLayout.add(new Image("images/logo.png", "SM logo"));
        logoLayout.add(new Icon(VaadinIcon.RECORDS));

        logoLayout.add(new H3("Projects"));
        layout.add(logoLayout, menu);
        return layout;

    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{
                createTab("Home", HomeView.class),
                createTab("Projects", ProjectListView.class),
                createTab("Organisations", OrganisationView.class),
                createTab("Master Data", MasterRefDataView.class),
                createTab("Users", UserView.class),
                createTab("Roles", RoleView.class),
                createTab("Permissions", PermissionView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

}
