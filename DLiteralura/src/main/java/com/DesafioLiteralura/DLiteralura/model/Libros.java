package com.DesafioLiteralura.DLiteralura.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
    public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    private String idioma;
    private Integer numeroDeDescargas;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id") )
    private List<Author> author;

        public Libros(){}

        public Libros(DatosLibros datosLibros) {
            this.titulo = datosLibros.titulo();
            setIdioma(datosLibros.idioma());
            this.numeroDeDescargas = datosLibros.numeroDeDescargas();
            this.author = datosLibros.autor().stream()
                    .map(datosAutor -> new Author(datosAutor))
                    .collect(Collectors.toList());
        }
        static class Utilidades {
            private static final
            Map<String, String> mapaIdiomas = new HashMap<>();
            static {
                mapaIdiomas.put("es", "Español");
                mapaIdiomas.put("en", "Inglés");
                mapaIdiomas.put("fr", "Francés");
                mapaIdiomas.put("fi", "Finlandés");
            }
            public static String convertirCodigoIdioma(String codigo) {
                return mapaIdiomas.getOrDefault(codigo, "Desconocido");
            }
        }
        public String getIdiomaCompleto() {
            return Utilidades.convertirCodigoIdioma(idioma);
        }
        @Override
        public String toString() {
            String autoresString = author.stream()
                    .map(Author::toString)
                    .collect(Collectors.joining(", "));
            return
                    String.format("""
                
                ********** LIBRO **********
                Titulo: %s
                Idioma: %s
                Descargas: %d
                %s
                ***************************
                """, titulo, getIdiomaCompleto(), numeroDeDescargas, autoresString);
        }

        public Long getId() {
        return Id;}

        public void setId(Long id) {
        Id = id;}

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public List<Author> getAuthor() {
            return author;
        }

        public void setAuthor(List<Author> author) {
            this.author = author;
        }

        public void setIdioma(String idioma) {
            this.idioma = idioma;
        }

        public List<String> getIdioma() {
            return Arrays.asList(idioma.split(","));
        }

        public void setIdioma(List<String> idioma) {
            this.idioma = String.join(",", idioma);
        }

        public Integer getNumeroDeDescargas() {
            return numeroDeDescargas;
        }

        public void setNumeroDeDescargas(Integer numeroDeDescargas) {
            this.numeroDeDescargas = numeroDeDescargas;
        }

    }
