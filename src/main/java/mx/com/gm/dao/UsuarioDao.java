package mx.com.gm.dao;

import mx.com.gm.domain.Usuario;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioDao extends JpaRepository<Usuario, Long>{
	Usuario findByUsername(String username);
	
	@Query(value = "SELECT u.id_usuario,u.username,u.password FROM radiomix.usuario u inner join radiomix.rol r on u.id_usuario=r.id_usuario where r.nombre= :PARAM", nativeQuery = true)
	List<Usuario> findAllByRoles(@Param("PARAM")String nombreRol);
}
