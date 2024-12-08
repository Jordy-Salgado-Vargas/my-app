package hn.proyectofinal.grupoone.controller;

import hn.proyectofinal.grupoone.data.Cursos;

public interface CursosInteractor {
	void consultarCursos();
	void agregarCurso(Cursos nuevo);
	void editarCurso(Cursos existente);
}