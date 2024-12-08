package hn.proyectofinal.grupoone.controller;

import hn.proyectofinal.grupoone.data.Empleados;

public interface EmpleadosInteractor {
	
	void consultarEmpleados();
	void agregarEmpleado(Empleados nuevo);
	void editarEmpleado(Empleados existente);
}