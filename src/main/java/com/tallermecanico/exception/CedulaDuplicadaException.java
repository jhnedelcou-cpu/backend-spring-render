package com.tallermecanico.exception;

public class CedulaDuplicadaException extends RuntimeException {

    public CedulaDuplicadaException(String cedula) {
        super("Ya existe un cliente con la cédula: " + cedula);
    }
}