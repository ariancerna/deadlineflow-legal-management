package com.utp.deadlineflow.repository;

import com.utp.deadlineflow.entity.EstadoTarea;
import com.utp.deadlineflow.entity.Tarea;
import com.utp.deadlineflow.entity.TipoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByExpedienteId(Long expedienteId);

    /**
     * Regla R4: existencia de tarea con mismo título (normalizado) y tipo,
     * dentro del mismo expediente, en un estado que aún se considera "activo".
     */
    @Query("""
           SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
           FROM Tarea t
           WHERE LOWER(TRIM(t.titulo)) = LOWER(TRIM(:titulo))
             AND t.tipo = :tipo
             AND t.expediente.id = :expedienteId
             AND t.estado NOT IN :estadosExcluidos
           """)
    boolean existsByTituloNormalizadoAndTipoAndExpedienteIdAndEstadoNotIn(
            @Param("titulo") String titulo,
            @Param("tipo") TipoTarea tipo,
            @Param("expedienteId") Long expedienteId,
            @Param("estadosExcluidos") List<EstadoTarea> estadosExcluidos);

    /** Usado por el Balance de Carga: cuenta tareas activas por responsable. */
    @Query("""
           SELECT t.responsable.id, COUNT(t)
           FROM Tarea t
           WHERE t.estado IN ('PENDIENTE', 'EN_PROCESO', 'EN_VALIDACION')
           GROUP BY t.responsable.id
           """)
    List<Object[]> contarTareasActivasPorResponsable();
}
