package com.utp.deadlineflow.controller;

import com.utp.deadlineflow.dto.request.ReasignacionRequestDTO;
import com.utp.deadlineflow.dto.response.CargaTrabajoResponseDTO;
import com.utp.deadlineflow.service.BalanceCargaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/balance-carga")
public class BalanceCargaController {

    private final BalanceCargaService balanceCargaService;

    public BalanceCargaController(BalanceCargaService balanceCargaService) {
        this.balanceCargaService = balanceCargaService;
    }

    @GetMapping
    public List<CargaTrabajoResponseDTO> obtenerBalance() {
        return balanceCargaService.calcularBalance();
    }

    @PostMapping("/reasignar")
    @PreAuthorize("hasAnyRole('COORDINADOR','ADMINISTRADOR')")
    public void reasignar(@Valid @RequestBody ReasignacionRequestDTO request) {
        // Delegado a una futura extensión de TareaService (APF2: persistencia + transacciones avanzadas)
        throw new UnsupportedOperationException("Reasignación se implementa en APF2 con persistencia completa");
    }
}
