package com.programacion4.unidad4ej6.fetchCotizacionDolarTests;

import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;
import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import com.programacion4.unidad4ej6.feature.insumo.services.impl.tasks.FetchCotizacionDolarTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FetchingBasicTests {
    @Mock
    private IInsumoRepository insumoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FetchCotizacionDolarTask task;

    @Test
    void update_deberiaActualizarInsumoCuandoCotizacionCambia() {
        double cotizacion = 1200.0;
        Insumo insumo = new Insumo(1L, "test", "COD000", 2L, true, 10.0, 1000.0, 10000.0);

        helper_configurarMockApi(cotizacion);
        when(insumoRepository.findAll()).thenReturn(List.of(insumo));

        task.fetchCotizacionDolar();

        assertEquals(1200.0, insumo.getValorDolarReferencia());
        assertEquals(12000.0, insumo.getPrecioEnPesos());

        verify(insumoRepository).saveAll(anyList());
        verify(insumoRepository, never()).save(any(Insumo.class)); // por saveAll
    }

    @Test
    void update_noDeberiaActualizarCuandoCotizacionEsIgual() {
        double cotizacion = 1200.0;
        Insumo insumo = new Insumo(1L, "test", "COD000", 2L, true, 10.0, 1200.0, 12000.0);

        helper_configurarMockApi(cotizacion);
        when(insumoRepository.findAll()).thenReturn(List.of(insumo));

        task.fetchCotizacionDolar();

        assertEquals(1200.0, insumo.getValorDolarReferencia());
        assertEquals(12000.0, insumo.getPrecioEnPesos());

        verify(insumoRepository, never()).saveAll(anyList());
    }

    @Test
    void exception_deberiaManejarErrorCuandoApiNoDevuelveCotizacion() {
        JsonNode respuestaInvalida = mock(JsonNode.class);

        when(respuestaInvalida.get("venta")).thenReturn(null);

        ResponseEntity<JsonNode> response = ResponseEntity.ok(respuestaInvalida);
        when(restTemplate.getForEntity(anyString(), eq(JsonNode.class)))
                .thenReturn(response);

        task.fetchCotizacionDolar();

        verify(insumoRepository, never()).findAll();
        verify(insumoRepository, never()).saveAll(anyList());
    }


    private void helper_configurarMockApi(double cotizacion) {
        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode ventaNode = mock(JsonNode.class);

        when(ventaNode.asDouble()).thenReturn(cotizacion);
        when(jsonNode.get("venta")).thenReturn(ventaNode);

        ResponseEntity<JsonNode> response = ResponseEntity.ok(jsonNode);
        when(restTemplate.getForEntity(anyString(), eq(JsonNode.class)))
                .thenReturn(response);
    }
}
