package com.DesafioLiteralura.DLiteralura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos (String json, Class<T> clase);
}
