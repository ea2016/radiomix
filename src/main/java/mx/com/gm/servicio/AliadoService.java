package mx.com.gm.servicio;

import java.util.List;

import mx.com.gm.domain.Aliado;

public interface AliadoService {
    
    public List<Aliado> listarAliados();
    
    public void guardar(Aliado aliado);
    
    public void eliminar(Aliado aliado);
    
    public Aliado encontrarAliados(Aliado aliado);
}
