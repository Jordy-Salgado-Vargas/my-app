package hn.proyectofinal.grupoone.services;

import hn.proyectofinal.grupoone.data.Empleados;
import hn.proyectofinal.grupoone.data.EmpleadosRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmpleadosService {

    private final EmpleadosRepository repository;

    public EmpleadosService(EmpleadosRepository repository) {
        this.repository = repository;
    }

    public Optional<Empleados> get(Long id) {
        return repository.findById(id);
    }

    public Empleados update(Empleados entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Empleados> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Empleados> list(Pageable pageable, Specification<Empleados> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
