package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;

import com.unla.ghsicilianotfi.entities.enums.TipoCosto;
import com.unla.ghsicilianotfi.entities.enums.TipoUnidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Evita hardcodear montos: cada festival define los suyos.
@Entity
@Getter @Setter @NoArgsConstructor
public class CostoFestival {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoCosto tipoCosto;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal monto;

	// null = se aplica a todas las unidades
	@Enumerated(EnumType.STRING)
	@Column(name = "aplica_a")
	private TipoUnidad aplicaA;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "festival_id", nullable = false)
	private Festival festival;

	public CostoFestival(Festival festival, TipoCosto tipoCosto, BigDecimal monto, TipoUnidad aplicaA) {
		this.festival = festival;
		this.tipoCosto = tipoCosto;
		this.monto = monto;
		this.aplicaA = aplicaA;
	}
}
