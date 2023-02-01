package mx.com.gm.servicio;

import java.util.List;
import mx.com.gm.dao.DescuentoDao;
import mx.com.gm.domain.Descuento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DescuentoServiceImpl implements DescuentoService{

    @Autowired
    private DescuentoDao descuentoDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Descuento> listarDescuento() {
        return (List<Descuento>) descuentoDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Descuento descuento) {
    	descuentoDao.save(descuento);
    }

    @Override
    @Transactional
    public void eliminar(Descuento descuento) {
    	descuentoDao.delete(descuento);
    }

    @Override
    @Transactional(readOnly = true)
    public Descuento encontrarDescuento(Descuento descuento) {
        return descuentoDao.findById(descuento.getIdDescuento()).orElse(null);
    }
    
    @Override
    public Descuento encontrarDescuento(String idDescuento) {
        return descuentoDao.findById(Long.parseLong(idDescuento)).orElse(null);
    }
}
