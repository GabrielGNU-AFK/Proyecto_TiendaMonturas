package gap.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gap.app.model.Orden;
import gap.app.model.Usuario;
import gap.app.service.IOrdenService;
import gap.app.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger=LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired 
	private IOrdenService ordenService;
	
	
	@Autowired
	private IUsuarioService usuarioService;
	
	List<Usuario> user= new ArrayList<Usuario>();
	
	BCryptPasswordEncoder passEncode= new BCryptPasswordEncoder();
	
	@GetMapping("/registro")
	private String create() {
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario,RedirectAttributes redirectAttrs) {
		logger.info(" Usuario: {}",usuario);
		try {
			
			usuario.setTipo("USER");
			usuario.setPassword(passEncode.encode(usuario.getPassword()));
			usuarioService.save(usuario);
			
			usuarioService.save(usuario);
			
			redirectAttrs
	         .addFlashAttribute("mensaje", "Agregado correctamente")
	         .addFlashAttribute("clase", "success");
			return "redirect:/";
			
			
		} catch (Exception e) {
		
			
				
			
		}
		
		redirectAttrs
        .addFlashAttribute("mensaje", "Email o Dni ya registrado pongase en contacto con el Administrador")
        .addFlashAttribute("clase", "danger");
		 
		
		
		
		return "redirect:/usuario/registro";
	}
	
	@GetMapping("/login")
	public String Login() {
		return "usuario/login";
	}
	
	@GetMapping("/acceder")
	public String accceder(Usuario usuario, HttpSession session) {
		logger.info("Acceso a : {}",usuario);
		
		Optional<Usuario> user=usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
		//logger.info("User db: {}",user.get());
	
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else {
				return "redirect:/";
			}
			
		}else {
			logger.info("No se encontro al usuario");
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model,HttpSession session) {
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes=ordenService.findByUsuario(usuario);
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
		
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		logger.info("Id orde: {}", id);
		
		Optional <Orden> orden=ordenService.findById(id);
		
		
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("Nombres",usuario.getNombre());
		
		model.addAttribute("Dni",usuario.getTelefono());
		model.addAttribute("Direccion",usuario.getDireccion());
		
		model.addAttribute("Total",orden.get().getTotal());
		
		model.addAttribute("NrBoleta",orden.get().getNumero());
		model.addAttribute("Fecha",orden.get().getFechaCreacion());
		
		model.addAttribute("usuario" ,usuario);
		model.addAttribute("detalles",orden.get().getDetalle());
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/detallecompra";
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		
		session.removeAttribute("idusuario");
		
		
		return "redirect:/";
	}
}
