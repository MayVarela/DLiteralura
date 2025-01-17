package com.DesafioLiteralura.DLiteralura.repository;

import com.DesafioLiteralura.DLiteralura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libros, Long> {
    Optional<Libros> findByTituloContainingIgnoreCase(String titulo);
    @Query("SELECT l FROM Libros l JOIN l.author a WHERE a.id = :autorId")
    List<Libros> findLibrosByAutorId(@Param("autorId") Long autorId);
}

