package com.unla.ghsicilianotfi.services.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.unla.ghsicilianotfi.dtos.FestivalDTO;
import com.unla.ghsicilianotfi.repositories.IFestivalRepository;
import com.unla.ghsicilianotfi.services.IFestivalService;

@Service("festivalService")
public class FestivalService implements IFestivalService {

	private final IFestivalRepository festivalRepository;

	private final ModelMapper modelMapper;

	public FestivalService(IFestivalRepository festivalRepository, ModelMapper modelMapper) {
		this.festivalRepository = festivalRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<FestivalDTO> getAll() {
		return festivalRepository.findAll().stream()
				.map(festival -> modelMapper.map(festival, FestivalDTO.class))
				.toList();
	}
}
