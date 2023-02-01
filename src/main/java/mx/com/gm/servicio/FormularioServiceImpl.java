package mx.com.gm.servicio;

import java.util.List;
import mx.com.gm.dao.FormularioDao;
import mx.com.gm.domain.Formulario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormularioServiceImpl implements FormularioService {

    @Autowired
    private FormularioDao formularioDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Formulario> listarFormulario() {
        return (List<Formulario>) formularioDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Formulario formulario) {
    	formularioDao.save(formulario);
    }

    @Override
    @Transactional
    public void eliminar(Formulario formulario) {
    	formularioDao.delete(formulario);
    }

    @Override
    @Transactional(readOnly = true)
    public Formulario encontrarFormulario(Formulario formulario) {
        return formularioDao.findById(formulario.getIdFormulario()).orElse(null);
    }

    @Override
	public List<Formulario> listarFormularioTecnico(String usuario) {
		return formularioDao.findByidTecnico(usuario);
	}
    
    @Override
	public List<Formulario> listarFormularioDoctor(String usuario) {
		return formularioDao.findByidDoctor(usuario);
	}

	@Override
	public Formulario encontrarFormulario(Long id) {
		return formularioDao.findById(id).orElse(null);
	}
}
