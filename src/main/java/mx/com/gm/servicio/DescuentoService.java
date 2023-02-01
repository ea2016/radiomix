package mx.com.gm.servicio;

import java.util.List;

import mx.com.gm.domain.Descuento;

public interface DescuentoService {
    
    public List<Descuento> listarDescuento();    
    
    public void guardar(Descuento descuento);
    
    public void eliminar(Descuento descuento);
    
    public Descuento encontrarDescuento(Descuento descuento);

    public Descuento encontrarDescuento(String idDescuento);

    public Descuento sacarDescuentoIndividual(String idExamen, String idAliado);
}
