package com.unla.ghsicilianotfi.entities;

import java.time.LocalDate;

import com.unla.ghsicilianotfi.entities.enums.RolCocina;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class EmpleadoCocina extends Personal {

	@Enumerated(EnumType.STRING)
	private RolCocina rol;

	private String tarea;

	public EmpleadoCocina(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
			LocalDate fechaIngreso, String email, RolCocina rol, String tarea) {
		super(nombre, apellido, dni, fechaNacimiento, fechaIngreso, email);
		this.rol = rol;
		this.tarea = tarea;
	}
}
