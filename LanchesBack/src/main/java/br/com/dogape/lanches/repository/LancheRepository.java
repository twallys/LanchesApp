package br.com.dogape.lanches.repository;

import br.com.dogape.lanches.model.Ingrediente;
import br.com.dogape.lanches.model.Lanche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancheRepository extends JpaRepository<Lanche, Long> {

    List<Lanche> findByIdIn(List<Long> lstId);

}
