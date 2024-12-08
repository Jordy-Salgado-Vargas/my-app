package hn.proyectofinal.grupoone.views.cursos;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import hn.proyectofinal.grupoone.data.Cursos;
import hn.proyectofinal.grupoone.services.CursosService;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Cursos")
@Route("master-detail/:cursosID?/:action?(edit)")
@Menu(order = 1, icon = LineAwesomeIconUrl.BOOK_READER_SOLID)
public class CursosView extends Div implements BeforeEnterObserver {

    private final String CURSOS_ID = "cursosID";
    private final String CURSOS_EDIT_ROUTE_TEMPLATE = "master-detail/%s/edit";

    private final Grid<Cursos> grid = new Grid<>(Cursos.class, false);

    private TextField cursoID;
    private TextField nombre;
    private TextField descripcion;
    private TextField duracion_Horas;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Cursos> binder;

    private Cursos cursos;

    private final CursosService cursosService;

    public CursosView(CursosService cursosService) {
        this.cursosService = cursosService;
        addClassNames("cursos-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("cursoID").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);
        grid.addColumn("duracion_Horas").setAutoWidth(true);
        grid.setItems(query -> cursosService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CURSOS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CursosView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Cursos.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(cursoID).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("cursoID");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.cursos == null) {
                    this.cursos = new Cursos();
                }
                binder.writeBean(this.cursos);
                cursosService.update(this.cursos);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(CursosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> cursosId = event.getRouteParameters().get(CURSOS_ID).map(Long::parseLong);
        if (cursosId.isPresent()) {
            Optional<Cursos> cursosFromBackend = cursosService.get(cursosId.get());
            if (cursosFromBackend.isPresent()) {
                populateForm(cursosFromBackend.get());
            } else {
                Notification.show(String.format("The requested cursos was not found, ID = %s", cursosId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(CursosView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        cursoID = new TextField("Curso ID");
        nombre = new TextField("Nombre");
        descripcion = new TextField("Descripcion");
        duracion_Horas = new TextField("Duracion_ Horas");
        formLayout.add(cursoID, nombre, descripcion, duracion_Horas);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
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

    private void populateForm(Cursos value) {
        this.cursos = value;
        binder.readBean(this.cursos);

    }
}
