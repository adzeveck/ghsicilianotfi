package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class CierreCaja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private LocalDate fecha;

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal totalRecaudado;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cajero_id")
	private Cajero cajero;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "participacion_id")
	private ParticipacionFestival participacion;

	public CierreCaja(LocalDate fecha, BigDecimal totalRecaudado, Cajero cajero, ParticipacionFestival participacion) {
		this.fecha = fecha;
		this.totalRecaudado = totalRecaudado;
		this.cajero = cajero;
		this.participacion = participacion;
	}
}
