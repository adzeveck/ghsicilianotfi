package com.unla.ghsicilianotfi.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UnidadVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String nombreComercial;

	@Column(name = "superficie_m2", nullable = false)
	private double superficieM2;

	// 10 caracteres, la logica de validacion va en un validador propio
	@Column(nullable = false, unique = true, length = 10)
	private String codigo;

	// Responsable: es un Personal (su rol de acceso es ROLE_RESPONSABLE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsable_id")
	private Personal responsable;

	@OneToMany(mappedBy = "unidadVenta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Plato> platos = new HashSet<>();

	@OneToMany(mappedBy = "unidadVenta", fetch = FetchType.LAZY)
	private Set<ParticipacionFestival> participaciones = new HashSet<>();

	protected UnidadVenta(String nombreComercial, double superficieM2, String codigo) {
		this.nombreComercial = nombreComercial;
		this.superficieM2 = superficieM2;
		this.codigo = codigo;
	}

	// Los montos se reciben del CostoFestival correspondiente (no hay valores fijos)
	public abstract BigDecimal calcularCanon(BigDecimal costoPorM2, BigDecimal costoPorMinutoMontaje,
			BigDecimal plusElectricidad);
}
