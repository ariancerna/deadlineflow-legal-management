package com.utp.deadlineflow.service;

import com.utp.deadlineflow.dto.request.TareaRequestDTO;
import com.utp.deadlineflow.entity.*;
import com.utp.deadlineflow.exception.TareaDuplicadaException;
import com.utp.deadlineflow.exception.OperacionNoPermitidaException;
import com.utp.deadlineflow.repository.ExpedienteRepository;
import com.utp.deadlineflow.repository.TareaRepository;
import com.utp.deadlineflow.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private ExpedienteRepository expedienteRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private BitacoraService bitacoraService;

    @InjectMocks private TareaService tareaService;

    @Test
    void debeRechazarTareaDuplicadaEnMismoExpediente() {
        TareaRequestDTO request = new TareaRequestDTO(
                "Presentar escrito de apelación", TipoTarea.PROCESAL,
                Prioridad.ALTA, 45L, 7L, LocalDate.now().plusDays(10));

        when(tareaRepository.existsByTituloNormalizadoAndTipoAndExpedienteIdAndEstadoNotIn(
                anyString(), eq(TipoTarea.PROCESAL), eq(45L), anyList()))
                .thenReturn(true);

        assertThatThrownBy(() -> tareaService.crear(request))
                .isInstanceOf(TareaDuplicadaException.class);

        verifyNoInteractions(expedienteRepository);
    }

    @Test
    void debePermitirTareaConMismoTituloEnExpedienteDistinto() {
        TareaRequestDTO request = new TareaRequestDTO(
                "Presentar escrito de apelación", TipoTarea.PROCESAL,
                Prioridad.MEDIA, 99L, 7L, LocalDate.now().plusDays(10));

        when(tareaRepository.existsByTituloNormalizadoAndTipoAndExpedienteIdAndEstadoNotIn(
                anyString(), eq(TipoTarea.PROCESAL), eq(99L), anyList()))
                .thenReturn(false);
        when(expedienteRepository.findById(99L))
                .thenReturn(Optional.of(Expediente.builder().id(99L).numeroExpediente("EXP-099").build()));
        when(usuarioRepository.findById(7L))
                .thenReturn(Optional.of(Usuario.builder().id(7L).nombre("María Torres").build()));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatCode(() -> tareaService.crear(request)).doesNotThrowAnyException();
        verify(tareaRepository).save(any(Tarea.class));
    }

    @Test
    void debeRechazarCierreDeTareaCriticaSinConfirmacion() {
        TareaService servicioSimple = new TareaService(mock(TareaRepository.class));
        Tarea tarea = Tarea.builder().id(1L).prioridad(Prioridad.ALTA)
                .estado(EstadoTarea.EN_VALIDACION).build();

        assertThatThrownBy(() -> servicioSimple.cambiarEstado(tarea, EstadoTarea.CERRADA, false))
                .isInstanceOf(OperacionNoPermitidaException.class)
                .hasMessageContaining("confirmación del supervisor");
    }

    @Test
    void debePermitirCierreDeTareaCriticaConConfirmacion() {
        TareaService servicioSimple = new TareaService(mock(TareaRepository.class));
        Tarea tarea = Tarea.builder().id(1L).prioridad(Prioridad.ALTA)
                .estado(EstadoTarea.EN_VALIDACION).build();

        Tarea resultado = servicioSimple.cambiarEstado(tarea, EstadoTarea.CERRADA, true);

        assertThat(resultado.getEstado()).isEqualTo(EstadoTarea.CERRADA);
    }

    @Test
    void debePermitirCierreDeTareaNoCriticaSinConfirmacion() {
        TareaService servicioSimple = new TareaService(mock(TareaRepository.class));
        Tarea tarea = Tarea.builder().id(2L).prioridad(Prioridad.BAJA)
                .estado(EstadoTarea.EN_PROCESO).build();

        assertThatCode(() -> servicioSimple.cambiarEstado(tarea, EstadoTarea.CERRADA, false))
                .doesNotThrowAnyException();
    }
}
