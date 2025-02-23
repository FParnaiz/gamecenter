package com.example.gamecenter;


import java.util.ArrayList;
import java.util.Random;



public class buscaminas_datos {


    int tablero_size;
    int numero_bombas;
    int[][] tablero;


    public buscaminas_datos(int tablero_size, int numero_bombas){

        this.tablero_size=tablero_size;
        this.numero_bombas= numero_bombas;
        this.tablero= new int[tablero_size][tablero_size];
        colocar_bombas();
        calcular_distancias();


    }



    private void colocar_bombas(){

        Random r = new Random();

        int i=0;
        while(i<numero_bombas){
            int x=r.nextInt(tablero_size);
            int y=r.nextInt(tablero_size);
            if(tablero[x][y]!=-1) {
                tablero[x][y] = -1;
                i++;
            }
        }
    }

    private void calcular_distancias(){


        for(int i =0;i<tablero_size;i++)
            for(int j=0;j<tablero_size;j++){
                int cont=0;
                if(tablero[i][j]!=-1){
                    // Arriba
                    if (i - 1 >= 0 && tablero[i - 1][j] == -1) {
                        cont++;
                    }

                    // Abajo
                    if (i + 1 < tablero_size && tablero[i + 1][j] == -1) {
                        cont++;
                    }

                    // Izquierda
                    if (j - 1 >= 0 && tablero[i][j - 1] == -1) {
                        cont++;
                    }

                    // Derecha
                    if (j + 1 < tablero_size && tablero[i][j + 1] == -1) {
                        cont++;
                    }

                    // Arriba-Izquierda (diagonal)
                    if (i - 1 >= 0 && j - 1 >= 0 && tablero[i - 1][j - 1] == -1) {
                        cont++;
                    }

                    // Arriba-Derecha (diagonal)
                    if (i - 1 >= 0 && j + 1 < tablero_size && tablero[i - 1][j + 1] == -1) {
                        cont++;
                    }

                    // Abajo-Izquierda (diagonal)
                    if (i + 1 < tablero_size && j - 1 >= 0 && tablero[i + 1][j - 1] == -1) {
                        cont++;
                    }

                    // Abajo-Derecha (diagonal)
                    if (i + 1 < tablero_size && j + 1 < tablero_size && tablero[i + 1][j + 1] == -1) {
                        cont++;
                    }

                    // Asignar el contador al tablero
                    tablero[i][j] = cont;

                }

            }

    }

    public int getValor( String nombre) {
        String[] parts = nombre.split("-");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return tablero[x][y];
    }

}
