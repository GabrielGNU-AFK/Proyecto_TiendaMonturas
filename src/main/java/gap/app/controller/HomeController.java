package gap.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import gap.app.model.DetalleOrden;
import gap.app.model.Orden;
import gap.app.model.Producto;
import gap.app.model.Usuario;
import gap.app.service.IDetalleOrdenService;
import gap.app.service.IOrdenService;
import gap.app.service.IUsuarioService;
import gap.app.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log=LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	//variable para el carrito 
	List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();
	
	//datos de la orden
	Orden orden = new Orden();
	
	
	
	@GetMapping("")
	public String home(Model model,HttpSession session) {
		
		log.info("Session de user :{}",session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoService.findActivos());
		model.addAttribute("productosvendidos", productoService.findTop5());
		//session 
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		return "usuario/home";
	}
	

	
	
	
	
	
	
	
	
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable  Integer id, Model model,HttpSession session) {
		log.info("Id enviado como parametro {}",id);
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		Producto producto = new Producto();
		Optional<Producto> producOptional= productoService.get(id);
		producto =  producOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad,Model model,HttpSession session) {
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		DetalleOrden detalleOrden = new DetalleOrden();
		
		Producto producto= new Producto();
		double sumaTotal=0;
		double igv=0.18;
		double igvP=0.0;
		Optional<Producto> optionalProducto= productoService.get(id);
		log.info("Producto:{}", optionalProducto);
		log.info("Cantidad:{}", cantidad);
		
		producto=optionalProducto.get();
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		
		detalleOrden.setTotal((producto.getPrecio()*cantidad)+((producto.getPrecio()*cantidad)*0.18));
		detalleOrden.setProducto(producto);
		
		
		igvP=detalleOrden.getPrecio()*0.18;
		//validacion para no agregar un producto mas de una vez
		Integer idProducto=producto.getId();
		//siencuentra alguna cocidencia con any match
		boolean ingresado=detalles.stream().anyMatch(p-> p.getProducto().getId()==idProducto); 
		
		if(!ingresado) {
			//añadimos los datos de nuestro detalles a nuestro objeto
			detalles.add(detalleOrden);
		}
		
		
		
		
		
		//funcion lambda para sumar todos los productos
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		//asignamosla variable al objeto
		orden.setTotal(Math.round(sumaTotal*100.0)/100.0);;
		igvP=sumaTotal*0.18;
		
		//agregamos a nuestro modelo los objetos 
		
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		return "usuario/carrito";
	}
	
	//QUITAR producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoOfCart(@PathVariable Integer id, Model model,HttpSession session) {
		
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		for(DetalleOrden detalleOrden:detalles) {
			if(detalleOrden.getProducto().getId() !=id) {
				ordenesNueva.add(detalleOrden);
				
			}
			
		}
		
		//ponemos la nueva lista con los productos
		detalles=ordenesNueva;
		
		double sumaTotal=0;
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(Math.round(sumaTotal*100.0)/100.0);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model, HttpSession session,RedirectAttributes redirectAttrs) {
		
		try {
			Usuario usuario=   usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
			
			
			if(orden.getTotal()<=0) {
				model.addAttribute("usuario",usuario);
				model.addAttribute("sesion",session.getAttribute("idusuario"));
				
				
				
				redirectAttrs
		        .addFlashAttribute("mensaje", "Necesita Agregar Productos al Carrito")
		        .addFlashAttribute("clase", "danger");
								
				
				return "redirect:/	";
				
				
			}else {
				
				model.addAttribute("cart",detalles);
				model.addAttribute("orden",orden);
				
				
				
				model.addAttribute("usuario",usuario);
				model.addAttribute("sesion",session.getAttribute("idusuario"));
				return "usuario/resumenorden";
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		redirectAttrs
        .addFlashAttribute("mensaje", "Necesita iniciar session para realizar una compra")
        .addFlashAttribute("clase", "danger");
		 
		
		
		
		return "redirect:/usuario/login";
	}
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session,RedirectAttributes redirectAttrs) {
		
		try {
			
			Date fechaCreacion = new Date();
			orden.setFechaCreacion(fechaCreacion);
			orden.setNumero(ordenService.generarNumeroOrden());
			
			//usuario
			Usuario usuario=   usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
			
			orden.setUsuario(usuario);
			ordenService.save(orden);
			//detalles a guardan
			
			//leemos cada detalle de nuestra lista
			for(DetalleOrden dt:detalles) {
				
				dt.setOrden(orden);
				detalleOrdenService.save(dt);
			}
			
			orden = new Orden();
			
			detalles.clear();
			return "redirect:/";
			
		} catch (Exception e) {
			
		}
		
		redirectAttrs
        .addFlashAttribute("mensaje", "Necesita iniciar session para realizar una compra")
        .addFlashAttribute("clase", "danger");
		 
		
		
		
		return "redirect:/usuario/login";
	
	}
	
	
	@GetMapping("/cancelOrder")
	public String cancelarOrder(HttpSession session) {

	
		//usuario
		Usuario usuario=   usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
	
		
		orden = new Orden();
		
		detalles.clear();
		return "redirect:/";
	}
	
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre ,Model model,HttpSession session) {
		log.info("Nombre del producto: {}",nombre);
		//obtenemos los productos                        funcion lambda  filtramos y atravez de la funcion anonima traemos el producto
		List<Producto> productos=productoService.findAll().stream().filter( p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productosvendidos", productoService.findTop5());
		model.addAttribute("productos", productos);
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		return "usuario/home";
	}
	
	@GetMapping("/metodoPago")
	public String metodoPago(Model model,HttpSession session) {
	
		Usuario usuario=   usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("usuario",usuario);
		model.addAttribute("sesion",session.getAttribute("idusuario"));
	
		return "usuario/metodopago";
	}
	
	
	
}
