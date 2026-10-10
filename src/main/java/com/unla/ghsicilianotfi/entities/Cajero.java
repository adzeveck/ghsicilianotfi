package com.unla.ghsicilianotfi.entities;

import java.time.LocalDate;

import com.unla.ghsicilianotfi.entities.enums.Turno;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Cajero extends Personal {

	@Enumerated(EnumType.STRING)
	private Turno turno;

	public Cajero(String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso,
			String email, Turno turno) {
		super(nombre, apellido, dni, fechaNacimiento, fechaIngreso, email);
		this.turno = turno;
	}
}
