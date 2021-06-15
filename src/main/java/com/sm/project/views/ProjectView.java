package com.sm.project.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route(value = "projects", layout = MainView.class)
@PageTitle("Projects")
public class ProjectView extends Div {

     public ProjectView() {
        addClassName("project-view");
        this.setHeightFull();
        this.add( getBox() );
     }

     private VerticalLayout getBox() {
        VerticalLayout box = new VerticalLayout();
        box.add(new Text("Version: 1.0"));
        box.add(new Text("Created By: Dermot Musker"));
        box.add(new Text("Released: 9th June 2021"));

        return box;
     }
}
