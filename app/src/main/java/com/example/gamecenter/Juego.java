package com.example.gamecenter;

public class Juego {
    private int id;
    private String nombre;

    public Juego(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre; // Esto permite que el Spinner muestre solo el nombre
    }
}
