package com.DesafioLiteralura.DLiteralura.principal;

import com.DesafioLiteralura.DLiteralura.model.Author;
import com.DesafioLiteralura.DLiteralura.model.DatosGeneral;
import com.DesafioLiteralura.DLiteralura.model.DatosLibros;
import com.DesafioLiteralura.DLiteralura.model.Libros;
import com.DesafioLiteralura.DLiteralura.repository.AuthorRepository;
import com.DesafioLiteralura.DLiteralura.repository.LibroRepository;
import com.DesafioLiteralura.DLiteralura.service.ConsumoApi;
import com.DesafioLiteralura.DLiteralura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new  ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private List<Libros> libros;
    private List<Author> autores;

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro
                    2 - Ver Lista de todos los libros buscados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Estadísticas de los libros registrados
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibrosBuscados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAnio();
                    break;
                case 5:
                    mostrarEstadisticas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion");
                    break;
                default:
                    System.out.println("Opcion Invalida");
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Por favor escriba el nombre del libro que desea buscar");
        String nombreLibro = teclado.nextLine();

        // Verifica si el libro ya existe en la base de datos
        Optional<Libros> libroExistente = libroRepository.findByTituloContainingIgnoreCase(nombreLibro);
        if (libroExistente.isPresent()) {
            System.out.println("Libro encontrado en la base de datos:");
            System.out.println(libroExistente.get().toString());
            return;
        }
        // Si el libro no existe en la base de datos, busca en la API
        DatosGeneral datos = getDatosGeneral(nombreLibro);
        if (datos != null && datos.resultado() != null && !datos.resultado().isEmpty()) {
            DatosLibros primerDato = datos.resultado().get(0);
            List<Author> autores = primerDato.autor().stream()
                    .map(datosAutor -> { Optional<Author> autorExistente = authorRepository.findByNombre(datosAutor.nombre());
                        return autorExistente.orElseGet(() -> new Author(datosAutor)); })
                    .collect(Collectors.toList());
            autores.forEach(authorRepository::save);

            Libros primerLibro = new Libros(primerDato);
            primerLibro.setAuthor(autores);
            libroRepository.save(primerLibro);
            System.out.println(primerLibro.toString());
        } else {
            System.out.println("No se encontraron datos para el libro especificado.");
        }
    }


    private void listarLibrosBuscados() {
        System.out.println("---Lista de libros registrados---");
        libros = libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("Aún no hay libros registrados en la base de datos");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libros::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados(){
        System.out.println("---Lista de autores registrados---");
        autores = authorRepository.findAll();
        if (autores.isEmpty()){
            System.out.println("Aún no hay autores registrados en la base de datos");
        } else {
            for (int i = 0; i < autores.size(); i++){
                List<Libros> librosPorAutor = libroRepository.findLibrosByAutorId(autores.get(i).getId());
                System.out.println("\n" + autores.get(i).toString());
                System.out.println("Libros registrados: ");
                librosPorAutor.forEach(l -> System.out.println("- " + l.getTitulo()));
            }
        }
    }

    private void listarAutoresVivosPorAnio(){
        System.out.println("Ingrese un año para buscar a los autores que seguían con vida");
        var anio = teclado.nextInt();
        List<Author> autoresVivos = authorRepository.buscarAutoresPorAnio(anio);
        System.out.println("---Autores registrados vivos en " + anio + "---");
        autoresVivos.forEach(a -> System.out.println("\n" + a.toString()));
    }

    private void mostrarEstadisticas() {
        System.out.println("---Estadísticas de los libros registrados---");
        libros = libroRepository.findAll();
        DoubleSummaryStatistics estadisticas = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0.0)
                .collect(Collectors.summarizingDouble(Libros::getNumeroDeDescargas));
        System.out.println("Máximo de descargas: " + estadisticas.getMax());
        System.out.println("Mínimo de descargas: " + estadisticas.getMin());
        System.out.println("Cantidad de libros registrados: " + estadisticas.getCount());
        System.out.println("Total de descargas de todos los libros registrados: " + estadisticas.getSum());
        System.out.println("Promedio de descargas: " + Math.round(estadisticas.getAverage()));
    }

    private DatosGeneral getDatosGeneral(String nombreLibro) {
        try {
            String json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
            return conversor.obtenerDatos(json, DatosGeneral.class);
        } catch (Exception e) {
            System.out.println("Error al obtener los datos: " + e.getMessage());
            return null;
        }
    }
}
