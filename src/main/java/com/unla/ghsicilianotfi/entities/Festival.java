package com.unla.ghsicilianotfi.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import com.unla.ghsicilianotfi.entities.enums.Temporada;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Festival {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String nombre;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Temporada temporada;

	@Column(nullable = false)
	private LocalDate fechaInicio;

	@Column(nullable = false)
	private LocalDate fechaFin;

	@OneToMany(mappedBy = "festival", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<CostoFestival> costos = new HashSet<>();

	@OneToMany(mappedBy = "festival", fetch = FetchType.LAZY)
	private Set<ParticipacionFestival> participaciones = new HashSet<>();

	public Festival(String nombre, Temporada temporada, LocalDate fechaInicio, LocalDate fechaFin) {
		this.nombre = nombre;
		this.temporada = temporada;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	// Cantidad de dias que dura el festival (inclusive)
	public long getDuracionDias() {
		return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
	}

	public boolean estaActivo(LocalDate fecha) {
		return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
	}
}
