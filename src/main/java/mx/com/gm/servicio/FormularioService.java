package mx.com.gm.servicio;

import java.util.List;

import mx.com.gm.domain.Formulario;

public interface FormularioService {
    
    public List<Formulario> listarFormulario();
    
    public void guardar(Formulario formulario);
    
    public void eliminar(Formulario formulario);
    
    public Formulario encontrarFormulario(Formulario formulario);
}
