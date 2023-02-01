package mx.com.gm.servicio;

import mx.com.gm.dao.DolarDao;
import mx.com.gm.domain.Dolar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DolarServiceImpl implements DolarService {

    @Autowired
    private DolarDao dolarDao;

	@Override
	public void guardar(Dolar dolar) {
		dolarDao.save(dolar);
		
	}
	@Override
	public int precioDolar() {
		List<Dolar> precio= dolarDao.findAll();
		return precio.get(0).getPrecio();
	}

}
