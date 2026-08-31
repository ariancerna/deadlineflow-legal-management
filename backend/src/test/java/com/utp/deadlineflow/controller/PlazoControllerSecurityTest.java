package com.utp.deadlineflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.deadlineflow.dto.response.PlazoResponseDTO;
import com.utp.deadlineflow.config.JwtUtil;
import com.utp.deadlineflow.config.SecurityConfig;
import com.utp.deadlineflow.service.UsuarioDetailsServiceImpl;
import com.utp.deadlineflow.entity.EstadoPlazo;
import com.utp.deadlineflow.service.PlazoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Prueba de contrato de API para la Regla R2 (restricción de escritura al rol AUDITOR). */
@WebMvcTest(PlazoController.class)
@Import(SecurityConfig.class)
class PlazoControllerSecurityTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private PlazoService plazoService;
    @MockBean private JwtUtil jwtUtil;
    @MockBean private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Test
    @WithMockUser(roles = "AUDITOR")
    void auditorNoPuedeAnularPlazo() throws Exception {
        mockMvc.perform(patch("/api/v1/plazos/1/anular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivo\":\"Intento no autorizado de anulación\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ABOGADO")
    void abogadoPuedeAnularPlazoConMotivo() throws Exception {
        when(plazoService.anular(eq(1L), anyString()))
                .thenReturn(new PlazoResponseDTO(1L, EstadoPlazo.ANULADO, LocalDate.now()));

        mockMvc.perform(patch("/api/v1/plazos/1/anular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivo\":\"Cliente desistió del proceso judicial\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ANULADO"));
    }

    @Test
    @WithMockUser(roles = "ABOGADO")
    void debeRechazarAnulacionConMotivoDemasiadoCorto() throws Exception {
        mockMvc.perform(patch("/api/v1/plazos/1/anular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivo\":\"corto\"}"))
                .andExpect(status().isBadRequest());
    }
}
