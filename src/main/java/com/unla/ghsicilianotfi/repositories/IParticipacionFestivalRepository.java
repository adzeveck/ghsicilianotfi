package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.ParticipacionFestival;

@Repository("participacionFestivalRepository")
public interface IParticipacionFestivalRepository extends JpaRepository<ParticipacionFestival, Integer> {

	List<ParticipacionFestival> findByFestivalId(int festivalId);

	List<ParticipacionFestival> findByUnidadVentaId(int unidadVentaId);

	Optional<ParticipacionFestival> findByFestivalIdAndUnidadVentaId(int festivalId, int unidadVentaId);

	// Participaciones en las que trabaja un empleado
	List<ParticipacionFestival> findByStaffId(int personalId);
}
