package com.utp.deadlineflow.repository;

import com.utp.deadlineflow.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query("""
           SELECT d FROM Documento d
           WHERE d.expediente.id = :expedienteId AND d.nombreArchivo = :nombreArchivo
           ORDER BY d.version DESC
           """)
    List<Documento> buscarVersionesPorNombre(@Param("expedienteId") Long expedienteId,
                                              @Param("nombreArchivo") String nombreArchivo);

    Optional<Documento> findFirstByExpedienteIdAndNombreArchivoOrderByVersionDesc(Long expedienteId, String nombreArchivo);

    @Query("""
           SELECT COUNT(d) FROM Documento d
           WHERE d.tipoDocumento IS NULL OR d.etiqueta IS NULL
           """)
    long contarSinClasificacionConsistente();
}
