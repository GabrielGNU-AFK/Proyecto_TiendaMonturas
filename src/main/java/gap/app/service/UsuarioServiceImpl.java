package gap.app.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gap.app.model.Usuario;
import gap.app.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private	IUsuarioRepository usuarioRepository;
	
	
	@Override
	public Optional<Usuario> findById(Integer id) {
		
		return usuarioRepository.findById(id);
	}


	@Override
	public Usuario save(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}


	@Override
	public Optional<Usuario> findByEmail(String email) {
		
		return usuarioRepository.findByEmail(email);
	}


	@Override
	public List<Usuario> findAll() {
		
		return usuarioRepository.findAll();
	}


	@Override
	public List<String> findByAllDataBeteween(LocalDate des, LocalDate has) {
		
		return usuarioRepository.findByAllDataBeteween(des, has);
	}
	
	

}
