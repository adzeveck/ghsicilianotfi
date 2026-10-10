package com.unla.ghsicilianotfi.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.unla.ghsicilianotfi.entities.enums.TipoUnidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Formulario de registro de un Responsable junto con su Unidad de Venta.
@Getter @Setter @NoArgsConstructor
public class RegistroResponsableDTO {

	// Datos del responsable
	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	private String apellido;

	@NotBlank(message = "El DNI es obligatorio")
	@Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener 7 u 8 digitos")
	private String dni;

	@NotNull(message = "La fecha de nacimiento es obligatoria")
	@Past(message = "La fecha de nacimiento debe ser anterior a hoy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaNacimiento;

	@NotBlank(message = "El email es obligatorio")
	@jakarta.validation.constraints.Email(message = "El email no es valido")
	private String email;

	// Credenciales
	@NotBlank(message = "El usuario es obligatorio")
	@Size(min = 4, max = 45, message = "El usuario debe tener entre 4 y 45 caracteres")
	private String usuario;

	@NotBlank(message = "La contrasena es obligatoria")
	@Size(min = 8, max = 60, message = "La contrasena debe tener entre 8 y 60 caracteres")
	private String password;

	@NotBlank(message = "Debe repetir la contrasena")
	private String confirmarPassword;

	// Datos de la unidad de venta
	@NotNull(message = "Debe elegir el tipo de unidad")
	private TipoUnidad tipoUnidad;

	@NotBlank(message = "El nombre comercial es obligatorio")
	private String nombreComercial;

	@NotNull(message = "La superficie es obligatoria")
	@Positive(message = "La superficie debe ser mayor a cero")
	private Double superficieM2;

	@NotBlank(message = "El codigo es obligatorio")
	@Size(min = 10, max = 10, message = "El codigo debe tener exactamente 10 caracteres")
	private String codigo;

	// Food Truck
	private String patente;

	private boolean requiereElectricidad;

	// Puesto Desarmable
	private Integer cantidadCarpas;

	private Integer minutosMontaje;

	// Festivales en los que participara la unidad
	private List<Integer> festivalIds = new ArrayList<>();
}
