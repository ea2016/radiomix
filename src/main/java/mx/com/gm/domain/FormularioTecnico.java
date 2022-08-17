package mx.com.gm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FormularioTecnico{
        
    private Long idFormulario;
    
    private String cedula;
    
    private String idTecnico;
    
    private String nombreExamen;

    private String idExamen;
    
    private String estatus;    
    
}
