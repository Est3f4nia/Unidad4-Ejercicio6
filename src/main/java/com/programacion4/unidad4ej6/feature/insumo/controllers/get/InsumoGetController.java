package com.programacion4.unidad4ej6.feature.insumo.controllers.get;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programacion4.unidad4ej6.feature.insumo.dtos.response.InsumoResponseDTO;
import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain.IInsumoGetService;
import com.programacion4.unidad4ej6.config.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/insumos")
@AllArgsConstructor
public class InsumoGetController {

    private final IInsumoGetService insumoGetService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<InsumoResponseDTO>>> all() {
        return ResponseEntity.ok(
                BaseResponse.ok(
                        insumoGetService.all(),
                        "Lista de productos obtenida correctamente."
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<InsumoResponseDTO>> getInsumo(
        @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.ok(
            insumoGetService.getInsumo(id), 
            "Insumo encontrado correctamente"
        ));
    }           
}
