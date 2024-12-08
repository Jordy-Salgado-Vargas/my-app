package hn.proyectofinal.grupoone.data;

import java.util.List;
import hn.proyectofinal.grupoone.data.Empleados;

public class EmpleadosResponse {

	private List<Empleados> items;
	private int count;
	
	public List<Empleados> getItems() {
		return items;
	}
	public void setItems(List<Empleados> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}