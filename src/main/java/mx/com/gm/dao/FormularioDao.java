package mx.com.gm.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.gm.domain.Formulario;

public interface FormularioDao extends JpaRepository<Formulario, Long>{
    
	 List<Formulario> findByidTecnico(String idTecnico);

}
