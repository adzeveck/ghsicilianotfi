package com.unla.ghsicilianotfi.services.implementation;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.ghsicilianotfi.dtos.RegistroResponsableDTO;
import com.unla.ghsicilianotfi.entities.FoodTruck;
import com.unla.ghsicilianotfi.entities.Festival;
import com.unla.ghsicilianotfi.entities.ParticipacionFestival;
import com.unla.ghsicilianotfi.entities.Personal;
import com.unla.ghsicilianotfi.entities.PuestoDesarmable;
import com.unla.ghsicilianotfi.entities.UnidadVenta;
import com.unla.ghsicilianotfi.entities.User;
import com.unla.ghsicilianotfi.entities.UserRole;
import com.unla.ghsicilianotfi.entities.enums.TipoUnidad;
import com.unla.ghsicilianotfi.helpers.CodigoUnidadValidator;
import com.unla.ghsicilianotfi.repositories.IFestivalRepository;
import com.unla.ghsicilianotfi.repositories.IParticipacionFestivalRepository;
import com.unla.ghsicilianotfi.repositories.IPersonalRepository;
import com.unla.ghsicilianotfi.repositories.IUnidadVentaRepository;
import com.unla.ghsicilianotfi.repositories.IUserRepository;
import com.unla.ghsicilianotfi.repositories.IUserRoleRepository;
import com.unla.ghsicilianotfi.services.IRegistroService;

@Service("registroService")
public class RegistroService implements IRegistroService {

	public static final String ROLE_RESPONSABLE = "ROLE_RESPONSABLE";
	private static final int EDAD_MINIMA = 18;

	private final IUserRepository userRepository;
	private final IUserRoleRepository userRoleRepository;
	private final IPersonalRepository personalRepository;
	private final IUnidadVentaRepository unidadVentaRepository;
	private final IFestivalRepository festivalRepository;
	private final IParticipacionFestivalRepository participacionRepository;
	private final PasswordEncoder passwordEncoder;

	public RegistroService(IUserRepository userRepository, IUserRoleRepository userRoleRepository,
			IPersonalRepository personalRepository,
			IUnidadVentaRepository unidadVentaRepository, IFestivalRepository festivalRepository,
			IParticipacionFestivalRepository participacionRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.personalRepository = personalRepository;
		this.unidadVentaRepository = unidadVentaRepository;
		this.festivalRepository = festivalRepository;
		this.participacionRepository = participacionRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Map<String, String> validar(RegistroResponsableDTO dto) {
		Map<String, String> errores = new LinkedHashMap<>();

		if (dto.getFechaNacimiento() != null
				&& Period.between(dto.getFechaNacimiento(), LocalDate.now()).getYears() < EDAD_MINIMA) {
			errores.put("fechaNacimiento", "El responsable debe ser mayor de edad");
		}
		if (dto.getDni() != null && personalRepository.existsByDni(dto.getDni())) {
			errores.put("dni", "Ya existe una persona registrada con ese DNI");
		}
		if (dto.getEmail() != null && personalRepository.existsByEmail(dto.getEmail())) {
			errores.put("email", "Ya existe una persona registrada con ese email");
		}
		if (dto.getUsuario() != null && userRepository.existsByUsername(dto.getUsuario())) {
			errores.put("usuario", "Ese nombre de usuario ya esta en uso");
		}
		if (dto.getPassword() != null && !dto.getPassword().equals(dto.getConfirmarPassword())) {
			errores.put("confirmarPassword", "Las contrasenas no coinciden");
		}
		validarUnidad(dto, errores);
		return errores;
	}

	private void validarUnidad(RegistroResponsableDTO dto, Map<String, String> errores) {
		if (dto.getTipoUnidad() == null) {
			return;
		}
		if (dto.getCodigo() != null && dto.getCodigo().length() == 10) {
			if (!CodigoUnidadValidator.esValido(dto.getCodigo(), dto.getTipoUnidad())) {
				errores.put("codigo", "El codigo debe ser " + CodigoUnidadValidator.prefijoDe(dto.getTipoUnidad())
						+ " seguido de 8 digitos");
			} else if (unidadVentaRepository.existsByCodigo(dto.getCodigo())) {
				errores.put("codigo", "Ya existe una unidad con ese codigo");
			}
		}
		if (dto.getTipoUnidad() == TipoUnidad.FOOD_TRUCK) {
			if (dto.getPatente() == null || dto.getPatente().isBlank()) {
				errores.put("patente", "La patente es obligatoria para un Food Truck");
			}
		} else {
			if (dto.getCantidadCarpas() == null || dto.getCantidadCarpas() <= 0) {
				errores.put("cantidadCarpas", "La cantidad de carpas debe ser mayor a cero");
			}
			if (dto.getMinutosMontaje() == null || dto.getMinutosMontaje() <= 0) {
				errores.put("minutosMontaje", "El tiempo de montaje debe ser mayor a cero");
			}
		}
	}

	@Override
	@Transactional
	public void registrar(RegistroResponsableDTO dto) {
		User user = new User(dto.getUsuario(), passwordEncoder.encode(dto.getPassword()), true);
		user = userRepository.save(user);
		UserRole rol = new UserRole();
		rol.setUser(user);
		rol.setRole(ROLE_RESPONSABLE);
		userRoleRepository.save(rol);

		Personal responsable = new Personal(dto.getNombre(), dto.getApellido(), dto.getDni(),
				dto.getFechaNacimiento(), LocalDate.now(), dto.getEmail());
		responsable.setUser(user);
		responsable = personalRepository.save(responsable);

		UnidadVenta unidad = dto.getTipoUnidad() == TipoUnidad.FOOD_TRUCK
				? new FoodTruck(dto.getNombreComercial(), dto.getSuperficieM2(), dto.getCodigo(),
						dto.getPatente(), dto.isRequiereElectricidad())
				: new PuestoDesarmable(dto.getNombreComercial(), dto.getSuperficieM2(), dto.getCodigo(),
						dto.getCantidadCarpas(), dto.getMinutosMontaje());
		unidad.setResponsable(responsable);
		unidad = unidadVentaRepository.save(unidad);

		for (Festival festival : festivalRepository.findAllById(dto.getFestivalIds())) {
			participacionRepository.save(new ParticipacionFestival(festival, unidad));
		}
	}
}
