package br.com.dogape.lanches.repository;

import br.com.dogape.lanches.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    List<Ingrediente> findByIdIn(List<Long> lstId);
}
