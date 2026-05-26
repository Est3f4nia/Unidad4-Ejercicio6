package com.programacion4.unidad4ej6.feature.insumo.controllers;

import com.programacion4.unidad4ej6.feature.insumo.dtos.request.InsumoCreateDTO;
import com.programacion4.unidad4ej6.feature.insumo.models.Insumo;
import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain.IInsumoCreateService;
import com.programacion4.unidad4ej6.feature.insumo.services.interfaces.domain.IInsumoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/insumos")
public class InsumoController {

    @Autowired
    private IInsumoCreateService insumoCreateService;

    @Autowired
    private IInsumoListService insumoListService;

    @GetMapping("/index")
    public String index(Model model) {
        String mensajeUsuario = "Bienvenido";
        LocalDateTime fechaActual = LocalDateTime.now();

        model.addAttribute("mensaje", mensajeUsuario);
        model.addAttribute("fecha", fechaActual);

        return "insumos/index";
    }

    @GetMapping("/form")
    public String mostrarFormulario(Model model) {
        model.addAttribute("insumo", new Insumo());
        return "insumos/form";
    }

    @PostMapping("/save")
    public String guardarInsumo(@ModelAttribute InsumoCreateDTO insumo) {

        insumoCreateService.createInsumo(insumo);

        return "redirect:/insumos/index";
    }

    @GetMapping("/listado")
    public String listarInsumos(Model model) {

        model.addAttribute("insumos", insumoListService.listInsumos());
        return "insumos/list";
    }
}
