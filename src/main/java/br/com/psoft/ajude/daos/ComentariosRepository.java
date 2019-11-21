package br.com.psoft.ajude.daos;

import br.com.psoft.ajude.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface ComentariosRepository<T, ID extends Serializable> extends JpaRepository<Comentario, Long> {

}
