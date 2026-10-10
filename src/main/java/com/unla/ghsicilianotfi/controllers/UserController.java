package com.unla.ghsicilianotfi.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unla.ghsicilianotfi.helpers.ViewRouteHelper;


@Controller
public class UserController {

	@GetMapping("/login")
	public String login(Model model,
						@RequestParam(required=false) String error,
						@RequestParam(required=false) String logout,
						@RequestParam(required=false) String registrado) {
		model.addAttribute("error", error);
		model.addAttribute("logout", logout);
		model.addAttribute("registrado", registrado);
		return ViewRouteHelper.USER_LOGIN;
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		return ViewRouteHelper.USER_LOGOUT;
	}

	@GetMapping("/loginsuccess")
	public String loginCheck(Authentication authentication) {
		// Cada rol aterriza en su propio panel
		return "redirect:" + switch (rolDe(authentication)) {
			case "ROLE_ADMIN" -> ViewRouteHelper.ADMIN_ROOT;
			case "ROLE_RESPONSABLE" -> ViewRouteHelper.RESPONSABLE_ROOT;
			case "ROLE_EMPLEADO" -> ViewRouteHelper.EMPLEADO_ROOT;
			default -> ViewRouteHelper.ROUTE;
		};
	}

	private String rolDe(Authentication authentication) {
		if (authentication == null) {
			return "";
		}
		return authentication.getAuthorities().stream()
				.map(authority -> authority.getAuthority())
				.findFirst()
				.orElse("");
	}
}
