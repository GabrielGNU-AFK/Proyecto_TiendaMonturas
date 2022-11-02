package gap.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gap.app.model.DetalleOrden;

public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {

}
