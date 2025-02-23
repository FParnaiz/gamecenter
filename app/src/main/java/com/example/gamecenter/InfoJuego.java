package com.example.gamecenter;

public class InfoJuego {
    private String titulo;
    private int record;
    private String activity;
    private String usuario;


    public InfoJuego(String titulo, int record, String usuario) {
        this.titulo = titulo;
        this.record = record;

        this.activity = "game_"+titulo.toLowerCase().replace(" ", "_");
        this.usuario = usuario;
    }


    public String getTitulo() {
        return titulo;
    }

    public String getActivity() {
        return activity;
    }

    public String getUsuario() {
        return usuario;
    }


    public int getRecord() {
        return record;
    }


}
