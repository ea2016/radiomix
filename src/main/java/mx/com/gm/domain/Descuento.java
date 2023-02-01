package mx.com.gm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "descuento")
public class Descuento implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDescuento;
    
    @NotEmpty
    private String idExamen;
    private String nombreExamen;
    
    @NotEmpty
    private String idAliado;
    private String nombreAliado;
    
    @NotEmpty
    private String descuento;
   
}
