package gap.app.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gap.app.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);
	
	@Query(value = "  select coalesce(sum(o.total),0 )as Total from ordenes o \r\n"
			+ "  where  o.fechacreacion\r\n"
			+ "  between ?1 and ?2 ",nativeQuery = true)
	 List<String> findByAllDataBeteween(LocalDate des,LocalDate has);
}
