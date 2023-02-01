package mx.com.gm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FormularioDoctor{
        
    private Long idFormulario;
    
    private String cedula;
    
    private String idDoctor;
    
    private String nombreExamen;

    private String idExamen;
    
    private String estatus;    
    
    private String rutaExamen;    
    
}
