package mx.com.gm.servicio;

import java.util.List;
import mx.com.gm.dao.AliadoDao;
import mx.com.gm.domain.Aliado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AliadoServiceImpl implements AliadoService {

    @Autowired
    private AliadoDao aliadoDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Aliado> listarAliados() {
        return (List<Aliado>) aliadoDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Aliado aliado) {
    	aliadoDao.save(aliado);
    }

    @Override
    @Transactional
    public void eliminar(Aliado aliado) {
    	aliadoDao.delete(aliado);
    }

    @Override
    @Transactional(readOnly = true)
    public Aliado encontrarAliados(Aliado aliado) {
        return aliadoDao.findById(aliado.getIdAliado()).orElse(null);
    }
}
