package hn.proyectofinal.grupoone.services;

import hn.proyectofinal.grupoone.data.Cursos;
import hn.proyectofinal.grupoone.data.CursosRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CursosService {

    private final CursosRepository repository;

    public CursosService(CursosRepository repository) {
        this.repository = repository;
    }

    public Optional<Cursos> get(Long id) {
        return repository.findById(id);
    }

    public Cursos update(Cursos entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Cursos> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Cursos> list(Pageable pageable, Specification<Cursos> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
