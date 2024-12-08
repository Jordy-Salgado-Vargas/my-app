package hn.proyectofinal.grupoone.views.empleados;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import hn.proyectofinal.grupoone.controller.EmpleadosInteractor;
import hn.proyectofinal.grupoone.controller.EmpleadosInteractorImpl;
import hn.proyectofinal.grupoone.data.Empleados;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Empleados")
@Route("/:empleadosID?/:action?(edit)")
@Menu(order = 0, icon = LineAwesomeIconUrl.ADDRESS_CARD)
@RouteAlias("")
public class EmpleadosView extends Div implements BeforeEnterObserver, EmpleadosViewModel {

    private final String EMPLEADO_ID = "empleadoID";
    private final String EMPLEADO_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Empleados> grid = new Grid<>(Empleados.class, false);

    private TextField empleadoid;
    private TextField nombre;
    private TextField apellido;
    private TextField correo;
    private TextField departamentoid;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private Empleados empleado;
    private List<Empleados> empleados;
    private EmpleadosInteractor controlador;

    public EmpleadosView() {
        addClassNames("empleados-view");
        controlador = new EmpleadosInteractorImpl(this);
        empleados = new ArrayList<>();
        
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn(Empleados::getEmpleadoid).setHeader("empleadoID").setAutoWidth(true);
        grid.addColumn(Empleados::getNombre).setHeader("nombre").setAutoWidth(true);
        grid.addColumn(Empleados::getApellido).setHeader("apellido").setAutoWidth(true);
        grid.addColumn(Empleados::getCorreo).setHeader("correo").setAutoWidth(true);
        grid.addColumn(Empleados::getDepartamentoid).setHeader("departamentoID").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            Empleados selectedEmpleado = event.getValue();
            if (selectedEmpleado != null) {
                System.out.println("ID seleccionado: " + selectedEmpleado.getEmpleadoid());
                populateForm(selectedEmpleado);
                UI.getCurrent().navigate(String.format(EMPLEADO_EDIT_ROUTE_TEMPLATE, selectedEmpleado.getEmpleadoid()));
            } else {
                System.out.println("No se seleccionó ningún empleado");
                clearForm();
                UI.getCurrent().navigate(EmpleadosView.class);
            }
        });

        // Configure Form
        //binder = new BeanValidationBinder<>(Empleados.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.forField(empleadoID).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                //.bind("empleadoID");
        //binder.forField(departamentoID).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                //.bind("departamentoID");

        //binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.empleado == null) {
                    this.empleado = new Empleados();
                    this.empleado.setNombre(nombre.getValue());
                    this.empleado.setApellido(apellido.getValue());
                    this.empleado.setCorreo(correo.getValue());
                    
                    this.controlador.agregarEmpleado(empleado);
                } else {
                    this.empleado.setNombre(nombre.getValue());
                    this.empleado.setApellido(apellido.getValue());
                    this.empleado.setCorreo(correo.getValue());
                    
                    this.controlador.editarEmpleado(empleado);
                }

                clearForm();
                refreshGrid();
                UI.getCurrent().navigate(EmpleadosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al actualizar los datos. Otra persona modificó el registro mientras estabas realizando cambios.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        controlador.consultarEmpleados();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> empleadoidParam = event.getRouteParameters().get(EMPLEADO_ID);
        if (empleadoidParam.isPresent()) {
            try {
                Integer empleadoid = Integer.parseInt(empleadoidParam.get());
                Empleados empleado = obtenerEmpleado(empleadoid);
                if (empleado != null) {
                    populateForm(empleado);
                } else {
                    Notification.show("No se encontró el empleado con ID " + empleadoid, 
                        3000, Position.MIDDLE);
                    clearForm();
                    refreshGrid();
                    // Considera usar UI.getCurrent().navigate() en lugar de event.forwardTo()
                    UI.getCurrent().navigate(EmpleadosView.class);
                }
            } catch (NumberFormatException e) {
                Notification.show("ID de empleado inválido", 
                    3000, Position.MIDDLE);
                clearForm();
                refreshGrid();
                // Considera usar UI.getCurrent().navigate() en lugar de event.forwardTo()
                UI.getCurrent().navigate(EmpleadosView.class);
            }
        }
    }

    private Empleados obtenerEmpleado(Integer id) {
        for (Empleados al : empleados) {
            if (al.getEmpleadoid().equals(id)) {
                return al;
            }
        }
        return null;
    }
    
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        empleadoid = new TextField("Empleado ID");
        empleadoid.setClearButtonVisible(true);
        empleadoid.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        nombre = new TextField("Nombre");
        nombre.setClearButtonVisible(true);
        nombre.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        apellido = new TextField("Apellido");
        apellido.setClearButtonVisible(true);
        apellido.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        correo = new TextField("Correo");
        correo.setClearButtonVisible(true);
        correo.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create
        ());
        
        departamentoid = new TextField("Departamento ID");
        departamentoid.setClearButtonVisible(true);
        departamentoid.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());

        formLayout.add(empleadoid, nombre, apellido, correo);

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
        controlador.consultarEmpleados();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Empleados value) {
        this.empleado = value;
        if (value == null) {
            empleadoid.setValue("");
            nombre.setValue("");
            apellido.setValue("");
            correo.setValue("");
            departamentoid.setValue("");
            // Deshabilitar campos cuando no hay selección
            nombre.setReadOnly(true);
            apellido.setReadOnly(true);
            correo.setReadOnly(true);
            save.setEnabled(false);
        } else {
            empleadoid.setValue(value.getEmpleadoid().toString());
            nombre.setValue(value.getNombre());
            apellido.setValue(value.getApellido());
            correo.setValue(value.getCorreo()); // Corregido aquí
            departamentoid.setValue(value.getDepartamentoid().toString());
            // Habilitar campos cuando hay selección
            nombre.setReadOnly(false);
            apellido.setReadOnly(false);
            correo.setReadOnly(false);
            save.setEnabled(true);
        }
        // El ID y departamentoid siempre será de solo lectura
        empleadoid.setReadOnly(true);
        departamentoid.setReadOnly(true);
    }

    
    @Override
    public void mostrarEmpleadosEnGrid(List<Empleados> items) {
    	Collection<Empleados> itemsCollection = items;
		this.empleados = items;
		grid.setItems(itemsCollection);
    }

	@Override
	public void mostrarMensajeError(String mensaje) {
		Notification notification = new Notification();
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

		Div text = new Div(new Text(mensaje));

		Button closeButton = new Button(new Icon("lumo", "cross"));
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		closeButton.setAriaLabel("Close");
		closeButton.addClickListener(event -> {
		    notification.close();
		});

		HorizontalLayout layout = new HorizontalLayout(text, closeButton);
		layout.setAlignItems(Alignment.CENTER);

		notification.add(layout);
		notification.open();
	}

	@Override
	public void mostrarMensajeExito(String mensaje) {
		Notification notification = Notification.show(mensaje);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		notification.open();
	}
}