package hn.proyectofinal.grupoone.views.cursos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import hn.proyectofinal.grupoone.controller.CursosInteractor;
import hn.proyectofinal.grupoone.controller.CursosInteractorImpl;
import hn.proyectofinal.grupoone.data.Cursos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Cursos")
@Route("master-detail/:cursosID?/:action?(edit)")
@Menu(order = 1, icon = LineAwesomeIconUrl.BOOK_READER_SOLID)
public class CursosView extends Div implements BeforeEnterObserver, CursosViewModel {

    private final String CURSO_ID = "cursosid";
    private final String CURSO_EDIT_ROUTE_TEMPLATE = "master-detail/%s/edit";

    private final Grid<Cursos> grid = new Grid<>(Cursos.class, false);

    private TextField cursoid;
    private TextField nombre;
    private TextField descripcion;
    private IntegerField duracion;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    //private final BeanValidationBinder<Cursos> binder;

    private Cursos curso;
    private List<Cursos> cursos;
    private CursosInteractor controlador;

    public CursosView() {
        addClassNames("cursos-view");
        controlador = new CursosInteractorImpl(this);
        cursos = new ArrayList<>();

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn(Cursos::getCursoid).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Cursos::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Cursos::getDescripcion).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(curso -> curso.getDuracion() != null ? curso.getDuracion().toString() : "null")
    .setHeader("Duración (Horas)")
    .setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            Cursos selectedCurso = event.getValue();
            if (selectedCurso != null) {
                System.out.println("ID seleccionado: " + selectedCurso.getCursoid());
                populateForm(selectedCurso);
                UI.getCurrent().navigate(String.format(CURSO_EDIT_ROUTE_TEMPLATE, selectedCurso.getCursoid()));
            } else {
                System.out.println("No se seleccionó ningún curso");
                clearForm();
                UI.getCurrent().navigate(CursosView.class);
            }
        });
        
        GridContextMenu<Cursos> menu = grid.addContextMenu();
        
        GridMenuItem<Cursos> generateReport = menu.addItem("Generar reporte", event -> {
        	// generar el reporte
        });
        
        GridMenuItem<Cursos> delete = menu.addItem("Eliminar Curso", event -> {
        	
        	if(event != null && event.getItem() != null) {
        		Cursos cursoEliminar = event.getItem().get();
            	if (cursoEliminar != null && cursoEliminar.getCursoid() != null) {
                    ConfirmDialog dialog = new ConfirmDialog();
                    dialog.setHeader("¿Eliminar a "+cursoEliminar.getNombre()+"?");
                    dialog.setText("¿Estás seguro que deseas eliminar de forma permanente a este curso?");

                    dialog.setCancelable(true);
                    dialog.setCancelText("No");
                    dialog.addCancelListener(eventDelete -> {});

                    dialog.setConfirmText("Si, Eliminar");
                    dialog.setConfirmButtonTheme("error primary");
                    dialog.addConfirmListener(eventDelete -> {
                        System.out.println("Eliminando curso con ID: " + cursoEliminar.getCursoid());
                        controlador.eliminarCurso(cursoEliminar.getCursoid().intValue());
                        refreshGrid();
                    });

                    dialog.open();
                } else {
                    Notification.show("No se puede eliminar un curso sin ID", 3000, Position.MIDDLE); 
                }
        	}
        });
        delete.addComponentAsFirst(createIcon(VaadinIcon.TRASH));

        // Configure Form
        //0binder = new BeanValidationBinder<>(Cursos.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.forField(cursoID).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                //.bind("cursoID");

        //binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.curso == null) {
                    this.curso = new Cursos();
                    this.curso.setNombre(nombre.getValue());
                    this.curso.setDescripcion(descripcion.getValue());
                    this.curso.setDuracion(duracion.getValue());
                    this.controlador.agregarCurso(curso);
                } else {
                    this.curso.setNombre(nombre.getValue());
                    this.curso.setDescripcion(descripcion.getValue());
                     this.curso.setDuracion(duracion.getValue());
                    this.controlador.editarCurso(curso);
                }

                clearForm();
                refreshGrid();
                UI.getCurrent().navigate(CursosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al actualizar los datos. Otra persona modificó el registro mientras estabas realizando cambios.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        controlador.consultarCursos();
    }
    
	private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> cursoidParam = event.getRouteParameters().get(CURSO_ID);
        if (cursoidParam.isPresent()) {
            try {
                Integer cursoid = Integer.parseInt(cursoidParam.get());
                Cursos curso = obtenerCurso(cursoid);
                if (curso != null) {
                    populateForm(curso);
                } else {
                    Notification.show("No se encontró el curso con ID " + cursoid, 
                        3000, Position.MIDDLE);
                    clearForm();
                    refreshGrid();
                    event.forwardTo(CursosView.class);
                }
            } catch (NumberFormatException e) {
                Notification.show("ID de curso inválido", 
                    3000, Position.MIDDLE);
                clearForm();
                refreshGrid();
                event.forwardTo(CursosView.class);
            }
        }
    }
    
    private Cursos obtenerCurso(Integer id) {
        for (Cursos al : cursos) {
            if (al.getCursoid().equals(id)) {
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
        cursoid = new TextField("Curso ID");
        cursoid.setClearButtonVisible(true);
        cursoid.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        nombre = new TextField("Nombre");
        nombre.setClearButtonVisible(true);
        nombre.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        descripcion = new TextField("Descripcion");
        descripcion.setClearButtonVisible(true);
        descripcion.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        duracion = new IntegerField("Duración (Horas)");  // Usar IntegerField para duracion
        duracion.setMin(1);  // Establecer un valor mínimo
        duracion.setClearButtonVisible(true);
        duracion.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());

        formLayout.add(cursoid, nombre, descripcion, duracion);

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
        grid.deselectAll();
        controlador.consultarCursos();
        clearForm();
    }

    private void clearForm() {
        populateForm(null);
    }

private void populateForm(Cursos value) {
    this.curso = value;

    if (value == null) {
        // Si no hay selección, los campos deben estar vacíos, pero solo `cursoid` debe ser readOnly
        cursoid.setValue("");
        nombre.setValue("");
        descripcion.setValue("");
        duracion.setValue(null);

        nombre.setReadOnly(false); // Nombre sigue siendo editable
        descripcion.setReadOnly(false); // Descripción sigue siendo editable
        duracion.setReadOnly(false); // Duración sigue siendo editable
    } else {
        // Si hay un curso seleccionado, los campos se llenan con sus valores
        cursoid.setValue(value.getCursoid().toString());
        nombre.setValue(value.getNombre());
        descripcion.setValue(value.getDescripcion());
        duracion.setValue(value.getDuracion());

        // Los campos nombre, descripción y duración serán editables
        nombre.setReadOnly(false);
        descripcion.setReadOnly(false);
        duracion.setReadOnly(false);
        save.setEnabled(true); // Botón guardar habilitado
    }

    // Solo el campo cursoid debe ser siempre solo lectura
    cursoid.setReadOnly(true);
}
    
    @Override
    public void mostrarCursosEnGrid(List<Cursos> items) {
    	Collection<Cursos> itemsCollection = items;
		this.cursos = items;
		grid.setItems(itemsCollection);
		
		items.forEach(curso -> System.out.println("Duración: " + curso.getDuracion()));
    }

	@Override
	public void mostrarMensajeError(String mensaje) {
		Notification notification = Notification.show(mensaje, 5000, Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);;

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
