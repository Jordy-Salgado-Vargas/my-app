package hn.proyectofinal.grupoone.data;

import java.util.List;

public class CursosResponse {
	private List<Cursos> items;
	private int count;
	public List<Cursos> getItems() {
		return items;
	}
	public void setItems(List<Cursos> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}