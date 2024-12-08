package hn.proyectofinal.grupoone.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class Empleados extends AbstractEntity {

    private Integer empleadoID;
    private String nombre;
    private String apellido;
    @Email
    private String correo;
    private Integer departamentoID;

    public Integer getEmpleadoID() {
        return empleadoID;
    }
    public void setEmpleadoID(Integer empleadoID) {
        this.empleadoID = empleadoID;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public Integer getDepartamentoID() {
        return departamentoID;
    }
    public void setDepartamentoID(Integer departamentoID) {
        this.departamentoID = departamentoID;
    }

}
