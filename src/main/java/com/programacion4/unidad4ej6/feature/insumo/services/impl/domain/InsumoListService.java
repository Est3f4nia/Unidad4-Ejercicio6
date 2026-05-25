package com.programacion4.unidad4ej6.feature.insumo.services.impl.domain;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain.IInsumoListService;

import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import com.programacion4.unidad4ej6.feature.insumo.mappers.InsumoMapper;
import com.programacion4.unidad4ej6.feature.insumo.dtos.response.InsumoResponseDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class InsumoListService implements IInsumoListService {
    
    private final IInsumoRepository insumoRepository;

    @Override
    public List<InsumoResponseDTO> listInsumos() {
        return InsumoMapper.toResponseDTOList(insumoRepository.findAllByActivoTrue());
    }
}
