package hn.proyectofinal.grupoone.views.empleados;

import java.util.List;

import hn.proyectofinal.grupoone.data.Empleados;

public interface EmpleadosViewModel {
	
	void mostrarEmpleadosEnGrid(List<Empleados> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}