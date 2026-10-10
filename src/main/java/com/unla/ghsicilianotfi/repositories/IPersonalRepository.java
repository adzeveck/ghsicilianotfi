package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.Personal;

@Repository("personalRepository")
public interface IPersonalRepository extends JpaRepository<Personal, Integer> {

	// Solo personal no eliminado logicamente
	List<Personal> findByEliminadoFalse();

	Optional<Personal> findByEmail(String email);

	Optional<Personal> findByDni(String dni);

	boolean existsByEmail(String email);

	boolean existsByDni(String dni);
}
