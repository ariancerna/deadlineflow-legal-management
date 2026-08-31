package com.utp.deadlineflow.controller;

import com.utp.deadlineflow.dto.request.ExpedienteRequestDTO;
import com.utp.deadlineflow.dto.response.ExpedienteResponseDTO;
import com.utp.deadlineflow.entity.EstadoExpediente;
import com.utp.deadlineflow.entity.Rol;
import com.utp.deadlineflow.service.ExpedienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expedientes")
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    public ExpedienteController(ExpedienteService expedienteService) {
        this.expedienteService = expedienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpedienteResponseDTO crear(@Valid @RequestBody ExpedienteRequestDTO request,
                                        Authentication authentication) {
        return expedienteService.crear(request, extraerRol(authentication));
    }

    @GetMapping("/{id}")
    public ExpedienteResponseDTO buscarPorId(@PathVariable Long id, Authentication authentication) {
        return expedienteService.buscarPorId(id, extraerRol(authentication));
    }

    @GetMapping
    public Page<ExpedienteResponseDTO> listar(@RequestParam(required = false) EstadoExpediente estado,
                                               Pageable pageable,
                                               Authentication authentication) {
        return expedienteService.listarPorEstado(
                estado != null ? estado : EstadoExpediente.ABIERTO, extraerRol(authentication), pageable);
    }

    private Rol extraerRol(Authentication authentication) {
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        return Rol.valueOf(authority.replace("ROLE_", ""));
    }
}
