package br.com.psoft.ajude.daos;

import br.com.psoft.ajude.entities.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface DoacoesRepository<T, ID extends Serializable> extends JpaRepository<Doacao, Long> {
}
