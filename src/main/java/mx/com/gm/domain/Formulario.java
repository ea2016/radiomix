package mx.com.gm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "formulario")
public class Formulario implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormulario;
    
    @NotEmpty
    private String cedula;
    @NotEmpty
    private String idAliado;
    @NotEmpty
    private String idTecnico;
    @NotEmpty
    private String tipoPago;
    @NotEmpty
    private String tipoEnvio;
    @NotEmpty
    private String idExamen;
    @NotEmpty
    private String estatus;
    
    private String rutaExamen;
    
    @NotEmpty
    private String idDoctor;
    
    private String informe;
    
    private String observaciones;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    
}
