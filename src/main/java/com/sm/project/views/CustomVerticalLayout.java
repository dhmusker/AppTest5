package com.sm.project.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class CustomVerticalLayout extends VerticalLayout implements BeforeEnterObserver {


    // When we first open this form, get the ID of the record to present from the URL
    @Override
    public void beforeEnter(BeforeEnterEvent event) {


    }

    public void navigateToParentView( Class viewclass ) {
        UI.getCurrent().navigate(viewclass);
    }

    public Div wrapForm(FormLayout formLayout ) {
        Div wrapper = new Div();
        wrapper.setClassName("sm-form-wrapper");
        wrapper.add(formLayout);
        return wrapper;
    }

}
