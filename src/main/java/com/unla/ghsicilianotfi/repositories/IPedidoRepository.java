package com.unla.ghsicilianotfi.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.ghsicilianotfi.entities.Pedido;

@Repository("pedidoRepository")
public interface IPedidoRepository extends JpaRepository<Pedido, Integer> {

	List<Pedido> findByParticipacionId(int participacionId);

	List<Pedido> findByParticipacionIdAndFechaBetween(int participacionId, LocalDate desde, LocalDate hasta);

	List<Pedido> findByParticipacionFestivalId(int festivalId);
}
