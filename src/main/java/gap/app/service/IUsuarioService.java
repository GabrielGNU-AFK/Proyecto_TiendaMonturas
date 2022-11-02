package gap.app.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import gap.app.model.Usuario;

public interface IUsuarioService {
	
	List<Usuario> findAll();
	Optional<Usuario> findById(Integer id);
	
	Usuario save(Usuario usuario);
	
	Optional<Usuario> findByEmail(String email);
	
	 List<String> findByAllDataBeteween(LocalDate desde,LocalDate hasta);
}
