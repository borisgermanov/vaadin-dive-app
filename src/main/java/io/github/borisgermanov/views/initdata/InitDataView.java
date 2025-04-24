package io.github.borisgermanov.views.initdata;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Init Data")
@Route("init-data")
@Menu(order = 3, icon = LineAwesomeIconUrl.DATABASE_SOLID)
@PermitAll
public class InitDataView extends Composite<VerticalLayout> {

    public InitDataView() {
        H5 h5 = new H5();
        ComboBox comboBox = new ComboBox();
        H5 h52 = new H5();
        ComboBox comboBox2 = new ComboBox();
        H5 h53 = new H5();
        ComboBox comboBox3 = new ComboBox();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h5.setText("Source Project");
        h5.setWidth("max-content");
        comboBox.setLabel("Source Project");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        h52.setText("Destination Project");
        h52.setWidth("max-content");
        comboBox2.setLabel("Destination Project");
        comboBox2.setWidth("min-content");
        setComboBoxSampleData(comboBox2);
        h53.setText("User");
        h53.setWidth("max-content");
        comboBox3.setLabel("User");
        comboBox3.setWidth("min-content");
        setComboBoxSampleData(comboBox3);
        getContent().add(h5);
        getContent().add(comboBox);
        getContent().add(h52);
        getContent().add(comboBox2);
        getContent().add(h53);
        getContent().add(comboBox3);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }
}
