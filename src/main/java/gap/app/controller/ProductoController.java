package gap.app.controller;


import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import gap.app.model.Producto;
import gap.app.model.Usuario;
import gap.app.service.IUsuarioService;
import gap.app.service.ProductoService;
import gap.app.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private ProductoService productoService;
	@Autowired
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
		
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img")  MultipartFile file,HttpSession session) throws IOException {
		LOGGER.info("VISTA OBJETO PRODUCTO {}",producto);
		
		
		Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(u);
		
		//imagen
		if (producto.getId()==null) { // cuando se crea un producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id,Model model) {
		Producto producto= new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);
		producto = optionalProducto.get();
		
		LOGGER.info(" PRODUCTO BUSCADO: {}",producto);
		model.addAttribute("producto", producto);
		
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto , @RequestParam("img")  MultipartFile file ) throws IOException {
	
		Producto p= new Producto();
		p=productoService.get(producto.getId()).get();
	
		
		if(file.isEmpty()) {
			
			producto.setImagen(p.getImagen());
		}
		else {//Editamos la imagen
			

			
			//ELIMINAMOS si la imagen no es la  pordefecto
			if(!p.getImagen().equals("default.jpg")) {
				upload.deleteImagen(p.getImagen());
			}
			
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);		
	
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		Producto producto= new Producto();
		
		Optional<Producto> optionalProducto=productoService.get(id);
		producto = optionalProducto.get();
		
		producto.setNombre("");
		
		if(!producto.getImagen().equals("default.jpg")) {
			upload.deleteImagen(producto.getImagen());
		}
		
		
		return "redirect:/productos";
	}
	
	
	
}
