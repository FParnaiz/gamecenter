package com.example.gamecenter;

import java.util.HashMap;
import java.util.Map;


public class MapaCasillas {

    private Map< Integer,Integer> mapa;

    public MapaCasillas(){
        mapa = new HashMap<>();
        mapa.put(0,R.drawable.ficha0);
        mapa.put(2,R.drawable.ficha2);
        mapa.put(4,R.drawable.ficha4);
        mapa.put(8,R.drawable.ficha8);
        mapa.put(16,R.drawable.ficha16);
        mapa.put(32,R.drawable.ficha32);
        mapa.put(64,R.drawable.ficha64);
        mapa.put(128,R.drawable.ficha128);
        mapa.put(256,R.drawable.ficha256);
        mapa.put(512,R.drawable.ficha512);
        mapa.put(1024,R.drawable.ficha1024);
        mapa.put(2048,R.drawable.ficha2048);
        mapa.put(4096,R.drawable.ficha4096);
        mapa.put(8192,R.drawable.ficha8192);
    }

    public int getFicha(int valor){
        return mapa.get(valor);
    }
}
