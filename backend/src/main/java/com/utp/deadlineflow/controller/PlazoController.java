package com.utp.deadlineflow.controller;

import com.utp.deadlineflow.dto.request.PlazoAnulacionRequestDTO;
import com.utp.deadlineflow.dto.request.PlazoRequestDTO;
import com.utp.deadlineflow.dto.response.PlazoResponseDTO;
import com.utp.deadlineflow.service.PlazoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * NOTA (R1): este controlador NO expone @DeleteMapping para /plazos/{id}.
 * La ausencia es intencional: SecurityConfig además bloquea DELETE en /api/v1/** con denyAll().
 */
@RestController
@RequestMapping("/api/v1/plazos")
public class PlazoController {

    private final PlazoService plazoService;

    public PlazoController(PlazoService plazoService) {
        this.plazoService = plazoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlazoResponseDTO crear(@Valid @RequestBody PlazoRequestDTO request) {
        return plazoService.crear(request);
    }

    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasAnyRole('ABOGADO','COORDINADOR','ADMINISTRADOR')")
    public PlazoResponseDTO anular(@PathVariable Long id, @Valid @RequestBody PlazoAnulacionRequestDTO request) {
        return plazoService.anular(id, request.motivo());
    }

    @GetMapping("/{id}/score-riesgo")
    public ScoreRiesgoResponse scoreRiesgo(@PathVariable Long id) {
        double score = plazoService.calcularScoreRiesgo(id);
        return new ScoreRiesgoResponse(score, nivel(score));
    }

    private String nivel(double score) {
        if (score >= 70) return "ALTO";
        if (score >= 40) return "MEDIO";
        return "BAJO";
    }

    public record ScoreRiesgoResponse(double score, String nivel) {}
}
