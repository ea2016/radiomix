package mx.com.gm.dao;

import mx.com.gm.domain.Examen;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamenDao extends JpaRepository<Examen, Long>{
    
}
