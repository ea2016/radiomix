package mx.com.gm.servicio;

import java.util.List;

import mx.com.gm.domain.Examen;

public interface ExamenService {
    
    public List<Examen> listarExamen();
    
    public void guardar(Examen examen);
    
    public void eliminar(Examen examen);
    
    public Examen encontrarExamen(Examen examen);
}
