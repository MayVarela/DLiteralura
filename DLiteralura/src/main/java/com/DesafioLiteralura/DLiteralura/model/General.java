package com.DesafioLiteralura.DLiteralura.model;

import java.util.List;

public class General {
    public List<Libros> resultado;

    public General(List<Libros> resultado) {
        this.resultado = resultado;
    }

    public List<Libros> getResultado() {
        return resultado;
    }

    public void setResultado(List<Libros> resultado) {
        this.resultado = resultado;
    }
}
