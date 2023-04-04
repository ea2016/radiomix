package mx.com.gm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.gm.domain.Descuento;

public interface DescuentoDao extends JpaRepository<Descuento, Long>{

	Descuento findByIdExamenAndIdAliado(String idExamen, String idAliado);
    
}
