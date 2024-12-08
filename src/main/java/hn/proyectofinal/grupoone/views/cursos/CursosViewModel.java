package hn.proyectofinal.grupoone.views.cursos;

import java.util.List;

import hn.proyectofinal.grupoone.data.Cursos;

public interface CursosViewModel {
	void mostrarCursosEnGrid(List<Cursos> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}