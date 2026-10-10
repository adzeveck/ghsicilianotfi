package com.unla.ghsicilianotfi.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.ghsicilianotfi.helpers.ViewRouteHelper;
import com.unla.ghsicilianotfi.services.IFestivalService;

// Paneles de inicio de cada rol. Los casos de uso se van colgando de cada panel.
@Controller
public class PanelController {

	private final IFestivalService festivalService;

	public PanelController(IFestivalService festivalService) {
		this.festivalService = festivalService;
	}

	@GetMapping(ViewRouteHelper.ADMIN_ROOT)
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView admin(Authentication authentication) {
		ModelAndView mAV = panel(ViewRouteHelper.ADMIN_INDEX, authentication);
		mAV.addObject("festivales", festivalService.getAll());
		return mAV;
	}

	@GetMapping(ViewRouteHelper.RESPONSABLE_ROOT)
	@PreAuthorize("hasRole('RESPONSABLE')")
	public ModelAndView responsable(Authentication authentication) {
		return panel(ViewRouteHelper.RESPONSABLE_INDEX, authentication);
	}

	@GetMapping(ViewRouteHelper.EMPLEADO_ROOT)
	@PreAuthorize("hasRole('EMPLEADO')")
	public ModelAndView empleado(Authentication authentication) {
		return panel(ViewRouteHelper.EMPLEADO_INDEX, authentication);
	}

	private ModelAndView panel(String view, Authentication authentication) {
		ModelAndView mAV = new ModelAndView(view);
		mAV.addObject("username", authentication.getName());
		return mAV;
	}
}
