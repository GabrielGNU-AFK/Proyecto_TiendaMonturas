package gap.app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gap.app.model.Producto;
import gap.app.repository.IProductoRepository;


@Service
public class ProductoServiceImpl implements ProductoService {
	
	//Inyectamos el objeto
	@Autowired
	private IProductoRepository productoRepository;	
	
	
	
	@Override
	public Producto save(Producto producto) {
		// TODO Auto-generated method stub
		return productoRepository.save(producto);
	}
	
	
	@Override
	public Optional<Producto> get(Integer id) {
		// TODO Auto-generated method stub
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		
		productoRepository.save(producto);
	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);
		
	}
	
	@Override
	public List<Producto> findAll() {
		return productoRepository.findAll();
	}


	@Override
	public List<Producto> findTop5() {
		return productoRepository.findTop5();
	}


	@Override
	@Transactional(readOnly=true)
	public Producto findOne(Integer id) {
		
		return productoRepository.getById(id);
	}


	@Override
	public List<Producto> findActivos() {
		// TODO Auto-generated method stub
		return productoRepository.findActivos();
	}




}
