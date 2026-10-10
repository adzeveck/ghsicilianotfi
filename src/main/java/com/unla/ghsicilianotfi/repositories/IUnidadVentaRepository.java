package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.UnidadVenta;

@Repository("unidadVentaRepository")
public interface IUnidadVentaRepository extends JpaRepository<UnidadVenta, Integer> {

	Optional<UnidadVenta> findByCodigo(String codigo);

	boolean existsByCodigo(String codigo);

	Optional<UnidadVenta> findByResponsableId(int responsableId);
}
