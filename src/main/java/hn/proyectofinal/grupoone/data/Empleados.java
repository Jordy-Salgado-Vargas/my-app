package hn.proyectofinal.grupoone.data;

public class Empleados extends AbstractEntity {

    private Integer empleadosid;
    private String nombre;
    private String apellido;
    private String email;
    private Integer departamentoid;

    public Integer getEmpleadosid() {
        return empleadosid;
    }
    public void setEmpleadosid(Integer empleadosd) {
        this.empleadosid = empleadosid;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getDepartamentoid() {
        return departamentoid;
    }
    public void setDepartamentoID(Integer departamentoid) {
        this.departamentoid = departamentoid;
    }

}
