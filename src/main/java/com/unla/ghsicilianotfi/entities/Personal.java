package com.unla.ghsicilianotfi.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Concreta: el Responsable es un Personal sin subclase (su rol de acceso es ROLE_RESPONSABLE)
@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Personal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String apellido;

	@Column(nullable = false, unique = true)
	private String dni;

	@Column(nullable = false)
	private LocalDate fechaNacimiento;

	@Column(nullable = false)
	private LocalDate fechaIngreso;

	@Column(nullable = false, unique = true)
	private String email;

	// baja logica
	private boolean eliminado = false;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany(mappedBy = "staff", fetch = FetchType.LAZY)
	private Set<ParticipacionFestival> participaciones = new HashSet<>();

	public Personal(String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso,
			String email) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaIngreso = fechaIngreso;
		this.email = email;
	}

	public int getAntiguedadAnios(LocalDate hasta) {
		return Period.between(fechaIngreso, hasta).getYears();
	}

	public boolean esMayorDeEdad(LocalDate hoy) {
		return Period.between(fechaNacimiento, hoy).getYears() >= 18;
	}
}
