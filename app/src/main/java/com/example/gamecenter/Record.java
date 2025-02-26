package com.example.gamecenter;

public class Record {
    private String nombreUsuario;
    private int puntuacion;

    public Record(String nombreUsuario, int puntuacion) {
        this.nombreUsuario = nombreUsuario;
        this.puntuacion = puntuacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    @Override
    public String toString() {
        return nombreUsuario + " - " + puntuacion;
    }
}
