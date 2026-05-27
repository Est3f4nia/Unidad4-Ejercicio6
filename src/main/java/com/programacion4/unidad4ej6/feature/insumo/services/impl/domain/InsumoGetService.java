package com.programacion4.unidad4ej6.feature.insumo.services.impl.domain;

import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain.IInsumoGetService;
import com.programacion4.unidad4ej6.feature.insumo.dtos.response.InsumoResponseDTO;
import com.programacion4.unidad4ej6.feature.insumo.mappers.InsumoMapper;
import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.commons.IInsumoFindByIdService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class InsumoGetService implements IInsumoGetService {
    
    private final IInsumoFindByIdService insumoFindByIdService;
    private final IInsumoRepository insumoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> all() {
        return StreamSupport.stream(insumoRepository.findAll().spliterator(), false)
                .map(InsumoMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDTO getInsumo(Long id) {
        return InsumoMapper.toResponseDTO(insumoFindByIdService.findByIdAndActivoTrue(id));
    }
}
