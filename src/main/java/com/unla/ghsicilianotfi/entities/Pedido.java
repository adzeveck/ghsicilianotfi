package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Festival y unidad de venta se obtienen a traves de la participacion
@Entity
@Getter @Setter @NoArgsConstructor
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private LocalDate fecha;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "participacion_id")
	private ParticipacionFestival participacion;

	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ItemPedido> items = new HashSet<>();

	public Pedido(LocalDate fecha, ParticipacionFestival participacion) {
		this.fecha = fecha;
		this.participacion = participacion;
	}

	public BigDecimal getTotal() {
		return items.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getCostoPlatos() {
		return items.stream().map(ItemPedido::getCosto).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
