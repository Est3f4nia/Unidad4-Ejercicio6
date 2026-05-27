package com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain;

import com.programacion4.unidad4ej6.feature.insumo.dtos.response.InsumoResponseDTO;

import java.util.List;

public interface IInsumoGetService {
    List<InsumoResponseDTO> all();
    InsumoResponseDTO getInsumo(Long id);
}
