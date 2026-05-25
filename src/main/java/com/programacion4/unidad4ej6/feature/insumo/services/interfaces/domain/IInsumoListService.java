package com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain;

import java.util.List;

import com.programacion4.unidad4ej6.feature.insumo.dtos.response.InsumoResponseDTO;

public interface IInsumoListService {
    
    List<InsumoResponseDTO> listInsumos();
}
