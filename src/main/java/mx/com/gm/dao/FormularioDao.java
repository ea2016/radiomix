package mx.com.gm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.com.gm.domain.Formulario;

public interface FormularioDao extends JpaRepository<Formulario, Long> {

	List<Formulario> findByidTecnico(String idTecnico);

	List<Formulario> findByidDoctor(String idDoctor);

	List<Formulario> findByCedula(String cedula);

	@Query(value = "SELECT f.tipo_pago, f.cedula, p.nombre, p.apellido From radiomix.formulario f inner join radiomix.persona p on p.cedula=f.cedula inner join radiomix.examen examen on examen.id_examen=f.id_examen where p.id_persona= ?1 and f.facturado=true", nativeQuery = true)
	List<Formulario> findAllByFactura(Long idPersona);

	 List<Formulario> findByidDoctor(String idDoctor);

}
