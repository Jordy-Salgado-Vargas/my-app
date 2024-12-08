package hn.proyectofinal.grupoone.views.empleados;

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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import hn.proyectofinal.grupoone.data.Empleados;
import hn.proyectofinal.grupoone.services.EmpleadosService;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Empleados")
@Route("/:empleadosID?/:action?(edit)")
@Menu(order = 0, icon = LineAwesomeIconUrl.ADDRESS_CARD)
@RouteAlias("")
public class EmpleadosView extends Div implements BeforeEnterObserver {

    private final String EMPLEADOS_ID = "empleadosID";
    private final String EMPLEADOS_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Empleados> grid = new Grid<>(Empleados.class, false);

    private TextField empleadoID;
    private TextField nombre;
    private TextField apellido;
    private TextField correo;
    private TextField departamentoID;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

   private final BeanValidationBinder<Empleados> binder;

    private Empleados empleados;

    private final EmpleadosService empleadosService;

    public EmpleadosView(EmpleadosService empleadosService) {
        this.empleadosService = empleadosService;
        addClassNames("empleados-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("empleadoID").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("apellido").setAutoWidth(true);
        grid.addColumn("correo").setAutoWidth(true);
        grid.addColumn("departamentoID").setAutoWidth(true);
        grid.setItems(query -> empleadosService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(EMPLEADOS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EmpleadosView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Empleados.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(empleadoID).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("empleadoID");
        binder.forField(departamentoID).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("departamentoID");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.empleados == null) {
                    this.empleados = new Empleados();
                }
                binder.writeBean(this.empleados);
                empleadosService.update(this.empleados);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(EmpleadosView.class);
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
        Optional<Long> empleadosId = event.getRouteParameters().get(EMPLEADOS_ID).map(Long::parseLong);
        if (empleadosId.isPresent()) {
            Optional<Empleados> empleadosFromBackend = empleadosService.get(empleadosId.get());
            if (empleadosFromBackend.isPresent()) {
                populateForm(empleadosFromBackend.get());
            } else {
                Notification.show(String.format("The requested empleados was not found, ID = %s", empleadosId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EmpleadosView.class);
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
        empleadoID = new TextField("Empleado ID");
        nombre = new TextField("Nombre");
        apellido = new TextField("Apellido");
        correo = new TextField("Correo");
        departamentoID = new TextField("Departamento ID");
        formLayout.add(empleadoID, nombre, apellido, correo, departamentoID);

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

    private void populateForm(Empleados value) {
        this.empleados = value;
        binder.readBean(this.empleados);

    }
}