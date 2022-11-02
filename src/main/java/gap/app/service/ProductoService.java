package gap.app.service;


import java.util.List;
import java.util.Optional;



import gap.app.model.Producto;

public interface ProductoService {
	
	public Producto save(Producto producto);
	//Optional para verificar si el producto existe o no
	public Optional<Producto> get(Integer id);
	public void update(Producto producto);
	public void delete(Integer id);
	
	public List<Producto> findAll();

	public List<Producto> findTop5();
	public List<Producto> findActivos();
	public Producto findOne(Integer id);
	

}
