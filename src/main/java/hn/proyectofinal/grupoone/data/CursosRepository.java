package hn.proyectofinal.grupoone.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CursosRepository extends JpaRepository<Cursos, Long>, JpaSpecificationExecutor<Cursos> {

}
