package com.sm.project.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends Div {

    public HomeView() {
        addClassName("home-view");
        this.setHeightFull();
        this.add( getBox() );
    }

    private VerticalLayout getBox() {
        VerticalLayout box = new VerticalLayout();
        box.add(new Text("Version: 1.0"),
                new Text("Created By: Dermot Musker"),
                new Text("Released: 9th June 2021"));

        return box;
    }
}
