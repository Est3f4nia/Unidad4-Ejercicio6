package com.programacion4.unidad4ej6.feature.insumo.services.impl.tasks;

import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;
import com.programacion4.unidad4ej6.feature.insumo.repositories.IInsumoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import java.util.List;


@Component
public class FetchCotizacionDolarTask {
    private static final Logger LOG = LoggerFactory.getLogger(FetchCotizacionDolarTask.class);

    private final IInsumoRepository  insumoRepository;
    private final RestTemplate restTemplate;

    public FetchCotizacionDolarTask(IInsumoRepository  insumoRepository, RestTemplate restTemplate) {
        this.insumoRepository = insumoRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 * * * MON-FRI")
    @Transactional
    public void fetchCotizacionDolar() {

        LOG.info("Iniciando actualización automática de cotización dólar");

        try {
            String url = "https://api.argentinadatos.com/v1/cotizaciones/dolares/oficial";

            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                LOG.error("Error al consumir API: {}", response.getStatusCode());
                return;
            }

            JsonNode body = response.getBody();

            if (body == null || body.get("venta") == null) {
                LOG.error("Cotizacion no encontrada");
                return;
            }

            double cotizacionDolar = body.get("venta").asDouble();
            LOG.info("Cotizacion obtenida: {}", cotizacionDolar);

            List<Insumo> insumos = insumoRepository.findAll();
            int actualizados = 0;

            for (Insumo insumo : insumos) {
                if (Math.abs(insumo.getValorDolarReferencia() - cotizacionDolar) > 0.01) {

                    double nuevoPrecioPesos = insumo.getPrecioEnDolares() * cotizacionDolar;

                    insumo.setValorDolarReferencia(cotizacionDolar);
                    insumo.setPrecioEnPesos(nuevoPrecioPesos);

                    LOG.info("Actualizando insumo ID {}: ${} → ${}", insumo.getId(), insumo.getPrecioEnPesos(), nuevoPrecioPesos);
                    actualizados++;
                }
            }

            if (actualizados > 0) {
                insumoRepository.saveAll(insumos);
                LOG.info("Se actualizaron {} insumos correctamente.", actualizados);
            } else {
                LOG.info("No había cambios en la cotización. No se actualizaron insumos.");
            }

        } catch (Exception e) {
            LOG.error("Error obteniendo cotizacion dolar", e);
        }
    }
}
