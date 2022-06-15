package mx.com.gm.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.gm.domain.Formulario;

public interface FormularioDao extends JpaRepository<Formulario, Long>{
    
}
