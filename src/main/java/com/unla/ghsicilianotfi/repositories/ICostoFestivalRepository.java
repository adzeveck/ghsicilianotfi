package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.CostoFestival;

@Repository("costoFestivalRepository")
public interface ICostoFestivalRepository extends JpaRepository<CostoFestival, Integer> {

	List<CostoFestival> findByFestivalId(int festivalId);
}
