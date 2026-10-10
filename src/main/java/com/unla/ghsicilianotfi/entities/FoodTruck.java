package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class FoodTruck extends UnidadVenta {

	private String patente;

	private boolean requiereElectricidad;

	public FoodTruck(String nombreComercial, double superficieM2, String codigo, String patente,
			boolean requiereElectricidad) {
		super(nombreComercial, superficieM2, codigo);
		this.patente = patente;
		this.requiereElectricidad = requiereElectricidad;
	}

	// areaSup * costoPorM2 + plus por electricidad (si corresponde)
	@Override
	public BigDecimal calcularCanon(BigDecimal costoPorM2, BigDecimal costoPorMinutoMontaje,
			BigDecimal plusElectricidad) {
		BigDecimal canon = costoPorM2.multiply(BigDecimal.valueOf(getSuperficieM2()));
		return requiereElectricidad ? canon.add(plusElectricidad) : canon;
	}
}
