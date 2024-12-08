package hn.proyectofinal.grupoone.data;

public class Empleados extends AbstractEntity {

    private Integer empleadoid;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer departamentoid;

    public Integer getEmpleadoid() {
        return empleadoid;
    }
    public void setEmpleadoID(Integer empleadoid) {
        this.empleadoid = empleadoid;
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
    public Integer getDepartamentoid() {
        return departamentoid;
    }
    public void setDepartamentoID(Integer departamentoid) {
        this.departamentoid = departamentoid;
    }

}
