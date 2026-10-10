package com.unla.ghsicilianotfi;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GhsicilianotfiApplicationTests {

	private static final String PASSWORD = "Epicentro2026";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {}

	@Test
	void inicioEsPublicoYMuestraLosFestivales() throws Exception {
		mockMvc.perform(get("/index"))
				.andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Festival de Primavera 2026")));
	}

	@Test
	void panelSinSesionRedirigeAlLogin() throws Exception {
		mockMvc.perform(get("/admin"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	void loginConCredencialesDelScriptEsExitoso() throws Exception {
		for (String usuario : new String[] { "admin", "julieta.paz@epicentro.test", "carlos.mendoza@epicentro.test" }) {
			mockMvc.perform(post("/loginprocess").param("username", usuario).param("password", PASSWORD))
					.andExpect(redirectedUrl("/loginsuccess"));
		}
	}

	@Test
	void loginConClaveIncorrectaFalla() throws Exception {
		mockMvc.perform(post("/loginprocess").param("username", "admin").param("password", "mala"))
				.andExpect(redirectedUrl("/login?error"));
	}

	@Test
	void cadaRolAterrizaEnSuPanel() throws Exception {
		mockMvc.perform(get("/loginsuccess").with(user("a").roles("ADMIN"))).andExpect(redirectedUrl("/admin"));
		mockMvc.perform(get("/loginsuccess").with(user("r").roles("RESPONSABLE"))).andExpect(redirectedUrl("/responsable"));
		mockMvc.perform(get("/loginsuccess").with(user("e").roles("EMPLEADO"))).andExpect(redirectedUrl("/empleado"));
	}

	@Test
	void cadaRolSoloAccedeASuPanel() throws Exception {
		mockMvc.perform(get("/admin").with(user("a").roles("ADMIN"))).andExpect(status().isOk());
		mockMvc.perform(get("/responsable").with(user("r").roles("RESPONSABLE"))).andExpect(status().isOk());
		mockMvc.perform(get("/empleado").with(user("e").roles("EMPLEADO"))).andExpect(status().isOk());

		mockMvc.perform(get("/admin").with(user("r").roles("RESPONSABLE"))).andExpect(status().isForbidden());
		mockMvc.perform(get("/responsable").with(user("e").roles("EMPLEADO"))).andExpect(status().isForbidden());
		mockMvc.perform(get("/empleado").with(user("a").roles("ADMIN"))).andExpect(status().isForbidden());
	}
}
