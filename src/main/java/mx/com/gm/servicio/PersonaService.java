package mx.com.gm.servicio;

import java.util.List;

import org.springframework.data.domain.Sort;

import mx.com.gm.domain.Persona;

public interface PersonaService {
    
    public List<Persona> listarPersonas(Sort sort);
    
    public void guardar(Persona persona);
    
    public void eliminar(Persona persona);
    
    public Persona encontrarPersona(Persona persona);

	List<Persona> listarPersonas();
}
