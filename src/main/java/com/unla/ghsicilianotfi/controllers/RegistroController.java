package com.unla.ghsicilianotfi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.ghsicilianotfi.dtos.RegistroResponsableDTO;
import com.unla.ghsicilianotfi.entities.enums.TipoUnidad;
import com.unla.ghsicilianotfi.helpers.ViewRouteHelper;
import com.unla.ghsicilianotfi.services.IFestivalService;
import com.unla.ghsicilianotfi.services.IRegistroService;

import jakarta.validation.Valid;

// CU03 - Registro (publico) de un Responsable con su Unidad de Venta
@Controller
@RequestMapping("/registro")
public class RegistroController {
	private static final String REGISTRO_ATTR = "registro";

	private final IRegistroService registroService;

	private final IFestivalService festivalService;

	public RegistroController(IRegistroService registroService, IFestivalService festivalService) {
		this.registroService = registroService;
		this.festivalService = festivalService;
	}

	@GetMapping("/responsable")
	public ModelAndView formulario() {
		return vista(new RegistroResponsableDTO());
	}

	@PostMapping("/responsable")
	public ModelAndView registrar(@Valid @ModelAttribute(REGISTRO_ATTR) RegistroResponsableDTO dto,
			BindingResult bindingResult) {
		registroService.validar(dto).forEach((campo, mensaje) -> {
			// un campo con error de anotacion conserva ese mensaje
			if (!bindingResult.hasFieldErrors(campo)) {
				bindingResult.rejectValue(campo, "registro." + campo, mensaje);
			}
		});
		if (bindingResult.hasErrors()) {
			return vista(dto);
		}
		registroService.registrar(dto);
		return new ModelAndView("redirect:/login?registrado");
	}

	private ModelAndView vista(RegistroResponsableDTO dto) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REGISTRO_RESPONSABLE);
		mAV.addObject(REGISTRO_ATTR, dto);
		mAV.addObject("festivales", festivalService.getAll());
		mAV.addObject("tiposUnidad", TipoUnidad.values());
		return mAV;
	}
}
