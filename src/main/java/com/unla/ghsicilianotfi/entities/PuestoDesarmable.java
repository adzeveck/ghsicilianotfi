package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class PuestoDesarmable extends UnidadVenta {

	private int cantidadCarpas;

	private int minutosMontaje;

	public PuestoDesarmable(String nombreComercial, double superficieM2, String codigo, int cantidadCarpas,
			int minutosMontaje) {
		super(nombreComercial, superficieM2, codigo);
		this.cantidadCarpas = cantidadCarpas;
		this.minutosMontaje = minutosMontaje;
	}

	// areaSup * costoPorM2 - minutosMontaje * costoPorMinutoMontaje
	@Override
	public BigDecimal calcularCanon(BigDecimal costoPorM2, BigDecimal costoPorMinutoMontaje,
			BigDecimal plusElectricidad) {
		return costoPorM2.multiply(BigDecimal.valueOf(getSuperficieM2()))
				.subtract(costoPorMinutoMontaje.multiply(BigDecimal.valueOf(minutosMontaje)));
	}
}
