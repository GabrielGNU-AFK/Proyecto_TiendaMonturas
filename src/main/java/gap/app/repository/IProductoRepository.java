package gap.app.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gap.app.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer>{

	
	@Query(value = "select p.* ,sum(d.cantidad) as total_quantity  from Productos p inner join  detalles d on d.producto_id=p.id where p.estado=\"Activo\" group by p.id,p.nombre order\r\n"
			+ "by SUM(d.cantidad) desc LIMIT 4",nativeQuery = true)
	List<Producto> findTop5();
	
	
	@Query(value="SELECT * FROM productos p where p.estado=\"Activo\"",nativeQuery = true)
	List<Producto> findActivos();
}
