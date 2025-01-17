package com.DesafioLiteralura.DLiteralura.repository;

import com.DesafioLiteralura.DLiteralura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.nombre ILIKE %:texto%")
    Optional<Author> findByNombre(@Param("texto") String texto);

    @Query("SELECT a FROM Author a WHERE a.fechaDeNacimiento < :anio AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento > :anio)")
    List<Author> buscarAutoresPorAnio(@Param("anio") int anio);
}