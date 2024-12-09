package hn.proyectofinal.grupoone.data;

public class Cursos extends AbstractEntity {

    private Integer cursoid;
    private String nombre;
    private String descripcion;
    private Integer duracion;

    public Integer getCursoid() {
        return cursoid;
    }
    public void setCursoid(Integer cursoid) {
        this.cursoid = cursoid;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Integer getDuracion() {
        return duracion;  
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}