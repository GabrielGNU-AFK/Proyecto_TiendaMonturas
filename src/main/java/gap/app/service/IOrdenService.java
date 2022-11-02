package gap.app.service;

import java.util.List;
import java.util.Optional;

import gap.app.model.Orden;
import gap.app.model.Usuario;

public interface IOrdenService {
	
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	Orden save(Orden orden);
	
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);	
}
