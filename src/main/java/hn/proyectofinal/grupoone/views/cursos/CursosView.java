package hn.proyectofinal.grupoone.views.cursos;

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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import hn.proyectofinal.grupoone.controller.CursosInteractor;
import hn.proyectofinal.grupoone.controller.CursosInteractorImpl;
import hn.proyectofinal.grupoone.data.Cursos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
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
    private TextField duracion_Horas;

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
        grid.addColumn(Cursos::getDuracion_Horas).setHeader("duracion_Horas").setAutoWidth(true);
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
                    
                    this.controlador.agregarCurso(curso);
                } else {
                    this.curso.setNombre(nombre.getValue());
                    this.curso.setDescripcion(descripcion.getValue());
                    
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
        
        duracion_Horas = new TextField("duracion_Horas");
        duracion_Horas.setClearButtonVisible(true);
        duracion_Horas.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());

        formLayout.add(cursoid, nombre, descripcion, duracion_Horas);

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
        controlador.consultarCursos();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Cursos value) {
    this.curso = value;
    if (value == null) {
        cursoid.setValue("");
        nombre.setValue("");
        descripcion.setValue("");
        duracion_Horas.setValue("");
        // Deshabilitar campos cuando no hay selección
        nombre.setReadOnly(true);
        descripcion.setReadOnly(true);
        duracion_Horas.setReadOnly(true);
        save.setEnabled(false);
    } else {
        cursoid.setValue(value.getCursoid().toString());
        nombre.setValue(value.getNombre());
        descripcion.setValue(value.getDescripcion());
        duracion_Horas.setValue(value.getDescripcion());
        // Habilitar campos cuando hay selección
        nombre.setReadOnly(false);
        descripcion.setReadOnly(false);
        duracion_Horas.setReadOnly(false);
        save.setEnabled(true);
    }
    // El ID siempre será de solo lectura
    cursoid.setReadOnly(true);
}
    
    @Override
    public void mostrarCursosEnGrid(List<Cursos> items) {
    	Collection<Cursos> itemsCollection = items;
		this.cursos = items;
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
