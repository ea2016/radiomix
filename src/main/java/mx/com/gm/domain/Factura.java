package mx.com.gm.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Factura{
    
    private String tipo_pago;
    private String cedula;
    private String nombre;
    private String apellido;
    
}
