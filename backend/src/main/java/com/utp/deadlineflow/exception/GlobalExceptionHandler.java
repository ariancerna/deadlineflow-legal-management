package com.utp.deadlineflow.exception;

import com.utp.deadlineflow.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TareaDuplicadaException.class)
    public ResponseEntity<ErrorResponseDTO> manejarDuplicado(TareaDuplicadaException ex) {
        return construir(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ErrorResponseDTO> manejarNoPermitido(OperacionNoPermitidaException ex) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> manejarAccesoDenegado(AccessDeniedException ex) {
        return construir(HttpStatus.FORBIDDEN, "No tiene permisos para realizar esta operación");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return construir(HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseEntity<ErrorResponseDTO> construir(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(
                new ErrorResponseDTO(status.value(), status.getReasonPhrase(), mensaje, LocalDateTime.now())
        );
    }
}
