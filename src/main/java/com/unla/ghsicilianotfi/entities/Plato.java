package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Plato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal precioVenta;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal costoProduccion;

	// baja logica
	private boolean eliminado = false;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "unidad_venta_id")
	private UnidadVenta unidadVenta;

	public Plato(String nombre, BigDecimal precioVenta, BigDecimal costoProduccion, UnidadVenta unidadVenta) {
		this.nombre = nombre;
		this.precioVenta = precioVenta;
		this.costoProduccion = costoProduccion;
		this.unidadVenta = unidadVenta;
	}
}
