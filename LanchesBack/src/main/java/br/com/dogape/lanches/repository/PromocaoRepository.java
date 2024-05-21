package br.com.dogape.lanches.repository;

import br.com.dogape.lanches.model.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

}
