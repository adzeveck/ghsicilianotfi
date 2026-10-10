package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.CierreCaja;

@Repository("cierreCajaRepository")
public interface ICierreCajaRepository extends JpaRepository<CierreCaja, Integer> {

	List<CierreCaja> findByParticipacionId(int participacionId);

	List<CierreCaja> findByCajeroId(int cajeroId);
}
