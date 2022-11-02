package gap.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gap.app.model.Orden;
import gap.app.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {
	
	List<Orden> findByUsuario(Usuario usuario);
	
}
