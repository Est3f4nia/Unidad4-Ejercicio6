package com.programacion4.unidad4ej6.config;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.programacion4.unidad4ej6.config.exceptions.CustomException;
import com.programacion4.unidad4ej6.feature.insumo.dtos.request.InsumoCreateDTO;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ATTR_MENSAJE_ERROR = "mensajeError";
    private static final String ATTR_ERRORES = "errores";
    private static final String ATTR_TITULO = "titulo";
    private static final String ATTR_MENSAJE = "mensaje";
    private static final String MODEL_INSUMO = "insumo";

    private static final String VIEW_ERROR = "error";
    private static final String VIEW_FORM = "insumos/form";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        BindingResult bindingResult = ex.getBindingResult();
        String objectName = bindingResult.getObjectName();

        model.addAttribute(objectName, bindingResult.getTarget());
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + objectName, bindingResult);
        model.addAttribute(ATTR_MENSAJE_ERROR, "Revise los datos del formulario");
        model.addAttribute(ATTR_ERRORES, fieldErrors(bindingResult));

        return VIEW_FORM;
    }

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, Model model, HttpServletResponse response) {
        if (ex.getStatus() == HttpStatus.CONFLICT) {
            return handleConflictOnForm(ex, model);
        }

        response.setStatus(ex.getStatus().value());
        model.addAttribute(ATTR_TITULO, tituloPorStatus(ex.getStatus()));
        model.addAttribute(ATTR_MENSAJE, ex.getMessage());
        model.addAttribute(ATTR_ERRORES, ex.getErrors());

        return VIEW_ERROR;
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        model.addAttribute(ATTR_TITULO, "Error inesperado");
        model.addAttribute(ATTR_MENSAJE, "Ocurrió un error inesperado");
        model.addAttribute(ATTR_ERRORES, List.of("Contacte al administrador"));

        return VIEW_ERROR;
    }

    private String handleConflictOnForm(CustomException ex, Model model) {
        model.addAttribute(ATTR_MENSAJE_ERROR, ex.getMessage());
        model.addAttribute(ATTR_ERRORES, ex.getErrors());

        if (!model.containsAttribute(MODEL_INSUMO)) {
            model.addAttribute(MODEL_INSUMO, new InsumoCreateDTO());
        }

        return VIEW_FORM;
    }

    private static List<String> fieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
    }

    private static String tituloPorStatus(HttpStatus status) {
        return switch (status) {
            case NOT_FOUND -> "No encontrado";
            case BAD_REQUEST -> "Solicitud inválida";
            case CONFLICT -> "Conflicto";
            default -> "Error";
        };
    }
}
