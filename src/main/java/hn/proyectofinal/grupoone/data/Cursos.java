package hn.proyectofinal.grupoone.data;

import jakarta.persistence.Entity;

@Entity
public class Cursos extends AbstractEntity {

    private Integer cursoID;
    private String nombre;
    private String descripcion;
    private String duracion_Horas;

    public Integer getCursoID() {
        return cursoID;
    }
    public void setCursoID(Integer cursoID) {
        this.cursoID = cursoID;
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
    public String getDuracion_Horas() {
        return duracion_Horas;
    }
    public void setDuracion_Horas(String duracion_Horas) {
        this.duracion_Horas = duracion_Horas;
    }

}
