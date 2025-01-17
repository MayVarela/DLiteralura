package com.DesafioLiteralura.DLiteralura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @ManyToMany(mappedBy = "author")
    private List<Libros> libros;

    public Author() {}

    public Author(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }


    public String voltearNombre(String nombre) {
        String[] partes = nombre.split(", ");
        if (partes.length == 2) {
            return partes[1] + " " + partes[0];
        } else {
            return nombre;
        }
    }
    public String verificadoFallecimiento(Integer fechaDeFallecimiento) {
        return (fechaDeFallecimiento != null) ? fechaDeFallecimiento.toString() : "--";
    }
    public String verificadoNacimiento(Integer fechaDeNacimiento) {
        return (fechaDeNacimiento != null) ? fechaDeNacimiento.toString() : "--";
    }

    @Override
    public String toString() {
        return String.format("""
                        Autor: %s
                        Fecha de Nacimiento: %s
                        Fecha de Fallecimiento: %s"""
                , voltearNombre(nombre), verificadoNacimiento(fechaDeNacimiento), verificadoFallecimiento(fechaDeFallecimiento));
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }
}
