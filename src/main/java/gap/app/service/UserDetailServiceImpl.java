package gap.app.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import gap.app.model.Usuario;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;
	
	//ENCRIPTAMOS LA CLAVE
	@Autowired
	private BCryptPasswordEncoder BCrypt;
	
	@Autowired
	HttpSession session;
	
	private Logger log= LoggerFactory.getLogger(UserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("User name: ");
		Optional<Usuario> optionalUser=usuarioService.findByEmail(username);
		if(optionalUser.isPresent()) {
			log.info("Este es el id : {}", optionalUser.get().getId());
			
			session.setAttribute("idusuario", optionalUser.get().getId());
			Usuario usuario=optionalUser.get();
			
			return User.builder().username
					(usuario.getNombre()).password(usuario.getPassword())
					.roles(usuario.getTipo()).build();
		}else {
			throw new UsernameNotFoundException("No se encontro el usuario");
		}
		
		
	}
	
	

}
