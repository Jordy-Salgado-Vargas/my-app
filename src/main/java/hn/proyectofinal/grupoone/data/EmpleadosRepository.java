package hn.proyectofinal.grupoone.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmpleadosRepository extends JpaRepository<Empleados, Long>, JpaSpecificationExecutor<Empleados> {

}
