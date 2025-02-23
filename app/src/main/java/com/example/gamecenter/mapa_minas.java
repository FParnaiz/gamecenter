package com.example.gamecenter;

import java.util.HashMap;

public class mapa_minas {

    private HashMap<Integer,Integer> mapa ;

    public mapa_minas(){
        mapa = new HashMap<>();
        mapa.put(-1,R.drawable.mine);
        mapa.put(0,R.drawable.empty0);
        mapa.put(1,R.drawable.empty1);
        mapa.put(2,R.drawable.empty2);
        mapa.put(3,R.drawable.empty3);
        mapa.put(4,R.drawable.empty4);
        mapa.put(5,R.drawable.empty5);
        mapa.put(6,R.drawable.empty6);
        mapa.put(7,R.drawable.empty7);
        mapa.put(8,R.drawable.empty8);

    }

    public int getFicha(int key){
        return mapa.get(key);
    }
}
