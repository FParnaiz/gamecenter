package com.example.gamecenter;

import android.content.Context;
import android.util.Log;

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
        Log.d("InfoMenu", "InfoMenu creado para el usuario: " + usuario);
        infoMenu.add(new InfoJuego("Records", 0, usuario));
        infoMenu.add(new InfoJuego("Ajustes", 0, usuario));

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



}
