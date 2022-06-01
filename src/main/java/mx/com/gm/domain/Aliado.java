package mx.com.gm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "aliado")
public class Aliado implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAliado;
    
    @NotEmpty
    private String nombre;
    
    @NotEmpty
    private String rif;
    
    @NotEmpty
    private String direccion;

}
