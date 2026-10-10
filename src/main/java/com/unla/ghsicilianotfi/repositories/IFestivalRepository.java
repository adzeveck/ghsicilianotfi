package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.Festival;

@Repository("festivalRepository")
public interface IFestivalRepository extends JpaRepository<Festival, Integer> {

	// Festivales en curso en una fecha dada
	List<Festival> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fecha, LocalDate mismaFecha);
}
