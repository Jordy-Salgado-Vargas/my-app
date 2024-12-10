package hn.proyectofinal.grupoone.controller;

import hn.proyectofinal.grupoone.data.Empleados;
import hn.proyectofinal.grupoone.data.EmpleadosResponse;
import hn.proyectofinal.grupoone.views.empleados.EmpleadosViewModel;
import hn.proyectofinal.grupoone.repository.DatabaseRepositoryImpl;

public class EmpleadosInteractorImpl implements EmpleadosInteractor {
	
	private DatabaseRepositoryImpl modelo;
	private EmpleadosViewModel vista;
	
	public EmpleadosInteractorImpl (EmpleadosViewModel vista) {
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 3000L);
	}
	
	@Override
	public void consultarEmpleados() {
	    try {
	        EmpleadosResponse respuesta = this.modelo.ConsultarEmpleados();
	        if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
	            this.vista.mostrarMensajeError("No hay Empleados disponibles");
	        } else {
	            this.vista.mostrarEmpleadosEnGrid(respuesta.getItems());
	        }
	    } catch(Exception error) {
	        error.printStackTrace();
	        this.vista.mostrarMensajeError("Error al consultar los Empleados: " + error.getMessage());
	    }
	}

	@Override
	public void agregarEmpleado(Empleados nuevo) {
		try {
			boolean creado = this.modelo.CrearEmpleados(nuevo);
			if(creado) {
				this.vista.mostrarMensajeExito("Empleado registrado exitosamente!");
			}else {
				this.vista.mostrarMensajeError("Hay un problema al Registrar el empleado");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	@Override
	public void editarEmpleado(Empleados existente) {
		try {
			boolean actualizado = this.modelo.ActualizarEmpleados(existente);
			if(actualizado) {
				this.vista.mostrarMensajeExito("¡Empleado modificado exitosamente!");
			}else {
				this.vista.mostrarMensajeError("Hay un problema al modificar el Empleado");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	@Override
	public void eliminarEmpleado(Integer id) {
	    try {
	        boolean eliminado = this.modelo.eliminarEmpleado(id);
	        if (eliminado) {
	            this.vista.mostrarMensajeExito("¡Empleado eliminado exitosamente!");
	        } else {
	            this.vista.mostrarMensajeError("Hay un problema al eliminar el empleado");
	        }
	    } catch (Exception error) {
	        error.printStackTrace();
	        this.vista.mostrarMensajeError("Error al eliminar el empleado: " + error.getMessage());
	    }
	}

}
	
	
