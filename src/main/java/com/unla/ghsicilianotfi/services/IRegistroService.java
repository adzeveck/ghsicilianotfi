package com.unla.ghsicilianotfi.services;

import java.util.Map;

import com.unla.ghsicilianotfi.dtos.RegistroResponsableDTO;

public interface IRegistroService {

	// Reglas de negocio que no se resuelven con anotaciones. Devuelve campo -> mensaje.
	Map<String, String> validar(RegistroResponsableDTO dto);

	// Crea el Responsable, su Unidad de Venta, su usuario (ROLE_RESPONSABLE) y las participaciones.
	void registrar(RegistroResponsableDTO dto);
}
