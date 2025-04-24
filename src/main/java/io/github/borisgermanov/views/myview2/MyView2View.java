package io.github.borisgermanov.views.myview2;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("My View 2")
@Route("my-view-2")
@Menu(order = 5, icon = LineAwesomeIconUrl.DATABASE_SOLID)
@RolesAllowed("USER")
public class MyView2View extends Composite<VerticalLayout> {

    public MyView2View() {
        getContent().setHeightFull();
        getContent().setWidthFull();
    }
}
