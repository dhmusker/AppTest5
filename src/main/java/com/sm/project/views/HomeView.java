package com.sm.project.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends VerticalLayout {

    public HomeView() {
        addClassName("home-view");
        this.setHeightFull();
        this.add( getBox() );
        this.setAlignItems(Alignment.CENTER);
    }

    private VerticalLayout getBox() {
        VerticalLayout box = new VerticalLayout();

        String content = "<div><h3>Project Listing</h3><p>Version: 1.0</p><p>Created By: Dermot Musker</p><p>Released: 9th June 2021</p></div>"; // wrapping <div> tags are required here
        Html html = new Html(content);
        Image img = new Image("images/DarkEmerald.jpg", "background image");

        box.add(img);
        box.add( html );

        return box;
    }
}
