package mx.com.gm.servicio;

import java.util.List;
import mx.com.gm.dao.ExamenDao;
import mx.com.gm.domain.Examen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

@Service
public class ExamenServiceImpl implements ExamenService {

    @Autowired
    private ExamenDao examenDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Examen> listarExamen() {
        return (List<Examen>) examenDao.findAll(Sort.by(Sort.Direction.DESC, "idExamen"));
    }

    @Override
    @Transactional
    public void guardar(Examen examen) {
    	examenDao.save(examen);
    }

    @Override
    @Transactional
    public void eliminar(Examen examen) {
    	examenDao.delete(examen);
    }

    @Override
    @Transactional(readOnly = true)
    public Examen encontrarExamen(Examen examen) {
        return examenDao.findById(examen.getIdExamen()).orElse(null);
    }
    
    @Override
    public Examen encontrarExamen(String idExamen) {
        return examenDao.findById(Long.parseLong(idExamen)).orElse(null);
    }
}
