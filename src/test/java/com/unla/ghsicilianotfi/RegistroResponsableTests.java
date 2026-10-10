package com.unla.ghsicilianotfi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.unla.ghsicilianotfi.entities.FoodTruck;
import com.unla.ghsicilianotfi.entities.Personal;
import com.unla.ghsicilianotfi.entities.PuestoDesarmable;
import com.unla.ghsicilianotfi.entities.UnidadVenta;
import com.unla.ghsicilianotfi.entities.enums.TipoUnidad;
import com.unla.ghsicilianotfi.helpers.CodigoUnidadValidator;
import com.unla.ghsicilianotfi.repositories.IParticipacionFestivalRepository;
import com.unla.ghsicilianotfi.repositories.IPersonalRepository;
import com.unla.ghsicilianotfi.repositories.IUnidadVentaRepository;

// CU03 - Registro de Responsable con Unidad de Venta
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RegistroResponsableTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private IUnidadVentaRepository unidadRepository;
	@Autowired
	private IPersonalRepository personalRepository;
	@Autowired
	private IParticipacionFestivalRepository participacionRepository;

	// Formulario valido de un Food Truck; cada test pisa lo que quiere probar
	private MockHttpServletRequestBuilder registro(String... pisar) {
		Map<String, String> campos = new LinkedHashMap<>();
		campos.put("nombre", "Laura");
		campos.put("apellido", "Gomez");
		campos.put("dni", "28123456");
		campos.put("fechaNacimiento", "1988-04-20");
		campos.put("email", "laura.gomez@test.com");
		campos.put("usuario", "lauragomez");
		campos.put("password", "ClaveSegura1");
		campos.put("confirmarPassword", "ClaveSegura1");
		campos.put("tipoUnidad", "FOOD_TRUCK");
		campos.put("nombreComercial", "Pastas Express");
		campos.put("superficieM2", "14.5");
		campos.put("codigo", "FT00000099");
		campos.put("patente", "AA111BB");
		campos.put("requiereElectricidad", "true");
		for (int i = 0; i < pisar.length; i += 2) {
			campos.put(pisar[i], pisar[i + 1]);
		}
		MockHttpServletRequestBuilder req = post("/registro/responsable").param("festivalIds", "1", "3");
		campos.forEach(req::param);
		return req;
	}

	@Test
	void formularioEsPublico() throws Exception {
		mockMvc.perform(get("/registro/responsable"))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.containsString("Registrar unidad de venta")));
	}

	@Test
	void registroValidoCreaResponsableUnidadYParticipaciones() throws Exception {
		mockMvc.perform(registro()).andExpect(redirectedUrl("/login?registrado"));

		UnidadVenta unidad = unidadRepository.findByCodigo("FT00000099").orElseThrow();
		assertTrue(unidad instanceof FoodTruck);
		assertTrue(((FoodTruck) unidad).isRequiereElectricidad());
		Personal responsable = unidad.getResponsable();
		assertEquals("laura.gomez@test.com", responsable.getEmail());
		assertEquals(2, participacionRepository.findByUnidadVentaId(unidad.getId()).size());
	}

	@Test
	void elNuevoResponsableAdentraPorLogin() throws Exception {
		mockMvc.perform(registro()).andExpect(redirectedUrl("/login?registrado"));
		mockMvc.perform(post("/loginprocess").param("username", "lauragomez").param("password", "ClaveSegura1"))
				.andExpect(redirectedUrl("/loginsuccess"));
	}

	@Test
	void registroDePuestoGuardaCarpasYMontaje() throws Exception {
		mockMvc.perform(registro("tipoUnidad", "PUESTO_DESARMABLE", "codigo", "PU00000098",
				"cantidadCarpas", "3", "minutosMontaje", "75"))
				.andExpect(redirectedUrl("/login?registrado"));
		PuestoDesarmable puesto = (PuestoDesarmable) unidadRepository.findByCodigo("PU00000098").orElseThrow();
		assertEquals(3, puesto.getCantidadCarpas());
		assertEquals(75, puesto.getMinutosMontaje());
	}

	@Test
	void usuarioRepetidoSeRechaza() throws Exception {
		mockMvc.perform(registro("usuario", "admin"))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.containsString("Ese nombre de usuario ya esta en uso")));
	}

	@Test
	void dniYEmailRepetidosSeRechazan() throws Exception {
		mockMvc.perform(registro("dni", "30111001", "email", "marcos.ledesma@epicentro.test"))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.containsString("Ya existe una persona registrada con ese DNI")))
				.andExpect(content().string(Matchers.containsString("Ya existe una persona registrada con ese email")));
	}

	@Test
	void menorDeEdadSeRechaza() throws Exception {
		mockMvc.perform(registro("fechaNacimiento", java.time.LocalDate.now().minusYears(17).toString()))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.containsString("El responsable debe ser mayor de edad")));
		assertFalse(unidadRepository.existsByCodigo("FT00000099"));
	}

	@Test
	void contrasenasDistintasOCortasSeRechazan() throws Exception {
		mockMvc.perform(registro("confirmarPassword", "otra"))
				.andExpect(content().string(Matchers.containsString("Las contrasenas no coinciden")));
		mockMvc.perform(registro("password", "corta", "confirmarPassword", "corta"))
				.andExpect(content().string(Matchers.containsString("entre 8 y 60 caracteres")));
	}

	@Test
	void codigoDebeRespetarFormatoYSerUnico() throws Exception {
		mockMvc.perform(registro("codigo", "XX00000099"))
				.andExpect(content().string(Matchers.containsString("El codigo debe ser FT seguido de 8 digitos")));
		mockMvc.perform(registro("codigo", "PU00000099"))
				.andExpect(content().string(Matchers.containsString("El codigo debe ser FT seguido de 8 digitos")));
		mockMvc.perform(registro("codigo", "FT00000001"))
				.andExpect(content().string(Matchers.containsString("Ya existe una unidad con ese codigo")));
		mockMvc.perform(registro("codigo", "FT123"))
				.andExpect(content().string(Matchers.containsString("exactamente 10 caracteres")));
	}

	@Test
	void camposDeCadaTipoDeUnidadSonObligatorios() throws Exception {
		mockMvc.perform(registro("patente", ""))
				.andExpect(content().string(Matchers.containsString("La patente es obligatoria")));
		mockMvc.perform(registro("tipoUnidad", "PUESTO_DESARMABLE", "codigo", "PU00000098"))
				.andExpect(content().string(Matchers.containsString("La cantidad de carpas debe ser mayor a cero")))
				.andExpect(content().string(Matchers.containsString("El tiempo de montaje debe ser mayor a cero")));
		mockMvc.perform(registro("superficieM2", "0"))
				.andExpect(content().string(Matchers.containsString("La superficie debe ser mayor a cero")));
	}

	@Test
	void validadorDeCodigo() {
		assertTrue(CodigoUnidadValidator.esValido("FT00000001", TipoUnidad.FOOD_TRUCK));
		assertTrue(CodigoUnidadValidator.esValido("PU12345678", TipoUnidad.PUESTO_DESARMABLE));
		assertFalse(CodigoUnidadValidator.esValido("PU12345678", TipoUnidad.FOOD_TRUCK));
		assertFalse(CodigoUnidadValidator.esValido("FT1234567A", TipoUnidad.FOOD_TRUCK));
		assertFalse(CodigoUnidadValidator.esValido(null, TipoUnidad.FOOD_TRUCK));
	}
}
