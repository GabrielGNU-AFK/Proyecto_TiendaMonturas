package gap.app.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gap.app.model.Orden;
import gap.app.model.Producto;
import gap.app.model.Usuario;
import gap.app.service.IOrdenService;
import gap.app.service.IUsuarioService;
import gap.app.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	@Autowired
	private ProductoService productoService;
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@GetMapping("")
	public String home(Model model) {
		
		List<Producto> productos=productoService.findAll();
		model.addAttribute("productos",productos);
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuario(Model model) {
		model.addAttribute("usuarios",usuarioService.findAll());
		return "administrador/usuarios";
	}
	
	/*
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		
		model.addAttribute("usuario",usuarioService.findAll());
		model.addAttribute("ordenes", ordenService.findAll());
		return "administrador/ordenes";
	}
	
	*/
	
	@GetMapping("/ordenes")
	public String listar(Model model) {

		//Lista---(ClienteDao->findAll())
		List<Usuario> listarUsuarios= usuarioService.findAll();
		model.addAttribute("ordenes", ordenService.findAll());
		model.addAttribute("listarUsuarios",listarUsuarios);
		
		return "administrador/ordenes";		
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model,@PathVariable Integer id) {
		
		Orden orden=ordenService.findById(id).get();
		model.addAttribute("detalles",orden.getDetalle());
		return "administrador/detalleorden";
	}
	
	

	@GetMapping("/reporteventa")
	public String mostrar() {
	      
	    return "administrador/reporteventa";

	} 
	
	@GetMapping("/reporteventa/{desde}{hasta}")
	@ResponseBody
	public String mostrar2(
	       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate 
	       desde,	
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate 
	       hasta, ModelAndView mp
	        ) throws ParseException {
	    mp.addObject("hola",usuarioService.findByAllDataBeteween(desde, hasta));
	    System.out.println(desde+"    "+hasta);
	    return "administrador/home";

	} 
	
	

	
}
