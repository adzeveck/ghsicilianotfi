package com.unla.ghsicilianotfi.dtos;

import java.time.LocalDate;

import com.unla.ghsicilianotfi.entities.enums.Temporada;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FestivalDTO {
	private int id;

	private String nombre;

	private Temporada temporada;

	private LocalDate fechaInicio;

	private LocalDate fechaFin;

	private long duracionDias;
}
