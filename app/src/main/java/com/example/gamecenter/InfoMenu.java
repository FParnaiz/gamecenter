package com.example.gamecenter;

import android.content.Context;

import java.util.ArrayList;

public class InfoMenu {

    private ArrayList<InfoJuego> infoMenu = new ArrayList<>();
    private String usuario;
    private DBhelper db ;
    private Context context;

    public InfoMenu(String usuario, Context context) {
        this.usuario = usuario;
        this.context=context;
        db=new DBhelper(context);
        infoMenu=generarInfoMenu();
        infoMenu.add(new InfoJuego("Ajustes", 0, "Ajustes"));
    }

    private ArrayList<InfoJuego> generarInfoMenu() {
        return db.getJuegos(usuario);
    }
    public ArrayList<InfoJuego> getJuegos() {
        return infoMenu;
    }
    public String getUsuario(){
        return usuario;
    }
    public InfoJuego getJuego(int position){
        return infoMenu.get(position);
    }


}
