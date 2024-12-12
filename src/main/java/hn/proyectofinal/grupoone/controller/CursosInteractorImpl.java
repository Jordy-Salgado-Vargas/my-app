package hn.proyectofinal.grupoone.controller;

import hn.proyectofinal.grupoone.data.Cursos;
import hn.proyectofinal.grupoone.data.CursosResponse;
import hn.proyectofinal.grupoone.views.cursos.CursosViewModel;
import hn.proyectofinal.grupoone.repository.DatabaseRepositoryImpl;

public class CursosInteractorImpl implements CursosInteractor {
//Hola si funciona.
	private DatabaseRepositoryImpl modelo;
	private CursosViewModel vista;
	
	public CursosInteractorImpl (CursosViewModel vista) {
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 3000L);
	}
	
	@Override
	public void consultarCursos() {
	    try {
	        CursosResponse respuesta = this.modelo.obtenerCursos();
	        if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
	            this.vista.mostrarMensajeError("No hay cursos disponibles");
	        } else {
	            this.vista.mostrarCursosEnGrid(respuesta.getItems());
	        }
	    } catch(Exception error) {
	        error.printStackTrace();
	        this.vista.mostrarMensajeError("Error al consultar los cursos: " + error.getMessage());
	    }
	}

	@Override
	public void agregarCurso(Cursos nuevo) {
		try {
			boolean creado = this.modelo.agregarCurso(nuevo);
			if(creado) {
				this.vista.mostrarMensajeExito("Curso registrado exitosamente!");
			}else {
				this.vista.mostrarMensajeError("Hay un problema al crear un Curso");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}

	@Override
	public void editarCurso(Cursos existente) {
		try {
			boolean actualizado = this.modelo.editarCurso(existente);
			if(actualizado) {
				this.vista.mostrarMensajeExito("Curso modificado exitosamente!");
			}else {
				this.vista.mostrarMensajeError("Hay un problema al modificar el curso");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}

	@Override
	public void eliminarCurso(Integer id) {
		try {
			System.out.println("CursosInteractor: Intentando eliminar curso con ID: " + id);
			boolean eliminado = this.modelo.eliminarCurso(id);
			System.out.println("CursosInteractor: Resultado de eliminación: " + eliminado);
			
			if (eliminado) {
				this.vista.mostrarMensajeExito("Curso eliminado correctamente!");
			} else {
				this.vista.mostrarMensajeError("No se pudo eliminar el curso. Puede que no exista.");
			}
		} catch (Exception error) {
			System.out.println("CursosInteractor: Error al eliminar el curso: ");
			error.printStackTrace();
			this.vista.mostrarMensajeError("Error al eliminar el curso: " + error.getMessage());
		}
	}
	
}