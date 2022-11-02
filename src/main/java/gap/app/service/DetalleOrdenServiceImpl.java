package gap.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gap.app.model.DetalleOrden;
import gap.app.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService{

	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;
	
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		// TODO Auto-generated method stub
		return detalleOrdenRepository.save(detalleOrden);
	}
	
	

}
