package com.unla.ghsicilianotfi.helpers;

import java.util.regex.Pattern;

import com.unla.ghsicilianotfi.entities.enums.TipoUnidad;

// Logica propia del codigo de 10 caracteres de una unidad de venta:
// 2 letras segun el tipo (FT = Food Truck, PU = Puesto Desarmable) + 8 digitos.
public class CodigoUnidadValidator {

	private static final Pattern FORMATO = Pattern.compile("^(FT|PU)\\d{8}$");

	private CodigoUnidadValidator() {}

	public static String prefijoDe(TipoUnidad tipo) {
		return tipo == TipoUnidad.FOOD_TRUCK ? "FT" : "PU";
	}

	public static boolean esValido(String codigo, TipoUnidad tipo) {
		return codigo != null && tipo != null
				&& FORMATO.matcher(codigo).matches()
				&& codigo.startsWith(prefijoDe(tipo));
	}
}
