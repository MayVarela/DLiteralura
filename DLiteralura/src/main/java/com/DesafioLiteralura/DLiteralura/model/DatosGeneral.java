package com.DesafioLiteralura.DLiteralura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosGeneral(
        @JsonAlias("results") List<DatosLibros> resultado
) {}
