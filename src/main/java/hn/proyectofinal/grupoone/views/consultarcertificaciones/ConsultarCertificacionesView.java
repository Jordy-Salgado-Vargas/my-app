package hn.proyectofinal.grupoone.views.consultarcertificaciones;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Consultar Certificaciones")
@Route("person-form")
@Menu(order = 3, icon = LineAwesomeIconUrl.ADDRESS_BOOK_SOLID)
public class ConsultarCertificacionesView extends Composite<VerticalLayout> {

    public ConsultarCertificacionesView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        ComboBox comboBox = new ComboBox();
        TextField textField = new TextField();
        TextArea textArea = new TextArea();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        TextField textField4 = new TextField();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("800px");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Información de cada certificación");
        h3.setWidth("100%");
        comboBox.setLabel("ID certificacion");
        comboBox.setWidth("160px");
        comboBox.getStyle().set("flex-grow", "1");
        setComboBoxSampleData(comboBox);
        textField.setLabel("Nombre de la certificación");
        textArea.setLabel("Descripción");
        textArea.setWidth("100%");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        textField2.setLabel("Validez");
        textField3.setLabel("Curso ID");
        textField4.setLabel("Empleado ID");
        buttonSecondary.setText("Limpiar");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(comboBox);
        layoutColumn2.add(textField);
        layoutColumn2.add(textArea);
        layoutColumn2.add(layoutRow);
        layoutRow.add(textField2);
        layoutRow.add(textField3);
        layoutRow.add(textField4);
        layoutColumn2.add(buttonSecondary);
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
