package com.example.gamecenter;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

public class TableroDatos {
    private int[][] tableroDatos; // Matriz del tablero
    private final int rows;
    private final int cols;
    private boolean swap = false;
    private int[] lastSpawn=new int[2];
    private int[][] previusTablero;
    private int score = 0;
    private int lastScore = 0;
    private game_2048 game;



    public TableroDatos(int rows, int cols, game_2048 game) {
        this.rows = rows;
        this.cols = cols;
        this.game =game;
        this.tableroDatos = new int[rows][cols];

        Log.d("Tablero", "Tablero inicializado");
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;

    }

    public int getLastScore() {
        return lastScore;
    }

    public int[][] getmatriz() {
        return tableroDatos;
    }
    private int[][] copyTablero(int[][] source) {
        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = source[i][j];
            }
        }
        return copy;
    }
    public int[][] generarTablero() {
        Log.d("Tablero", "Generando tablero");
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tableroDatos[i][j] = 0;
            }
        }
        int pos1 = random.nextInt(rows);
        int pos2 = random.nextInt(cols);
        tableroDatos[pos1][pos2] = 2;
        lastSpawn[0]=pos1;
        lastSpawn[1]=pos2;
        previusTablero=copyTablero(tableroDatos);
        return tableroDatos;
    }

    public int[][] actualizarTablero() {
        Random random = new Random();
        ArrayList<int[]> vacios = new ArrayList<>();

        // Buscar todos los espacios vacíos
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tableroDatos[i][j] == 0) {
                    vacios.add(new int[]{i, j});
                }
            }
        }

        // Verificar si hay espacios vacíos
        if (!vacios.isEmpty()) {
            int[] newSpawn;
            boolean fichaGenerada = false;

            // Intentamos generar una ficha nueva en una posición diferente de la última
            for (int i = 0; i < vacios.size(); i++) {
                newSpawn = vacios.get(random.nextInt(vacios.size()));

                // Aseguramos que no se genere la ficha en la misma posición que la anterior
                if (newSpawn[0] != lastSpawn[0] || newSpawn[1] != lastSpawn[1]) {
                    tableroDatos[newSpawn[0]][newSpawn[1]] = (random.nextInt(6) == 0) ? 4 : 2;
                    lastSpawn = newSpawn; // Actualizar lastSpawn con la nueva posición
                    fichaGenerada = true;
                    break; // Solo una ficha nueva se debe generar
                }
            }

            // Si no se generó la ficha en un ciclo, esto se podría manejar como un error o un ajuste
            if (!fichaGenerada) {
                Log.d("Tablero", "No se pudo generar una nueva ficha diferente de la anterior");
            }
        } else {
            Log.d("Tablero", "No hay espacios vacíos");
        }

        return tableroDatos;
    }


    public int[][] moverCasillas(String direccion) {
        swap = false;
        previusTablero=copyTablero(tableroDatos);
        lastScore=getScore();// Reinicia el estado de swap
        switch (direccion) {
            case "a": moverIzquierda(); break;
            case "d": moverDerecha(); break;
            case "w": moverArriba(); break;
            case "s": moverAbajo(); break;
        }
        if (swap) {
            actualizarTablero();
        }
        if(game.getWin() && !game.getEndless()){
            game.ganar();

        }
        if (hasPerdido()) {
            Log.d("Tablero", "¡Juego terminado! No hay más movimientos posibles.");
            game.perder(score);

        }
        return tableroDatos;
    }

    private void moverIzquierda() {
        for (int i = 0; i < rows; i++) {
            int pos = 0; // Posición para insertar números no cero
            for (int j = 0; j < cols; j++) {
                if (tableroDatos[i][j] != 0) {
                    if (pos > 0 && tableroDatos[i][pos - 1] == tableroDatos[i][j]) {
                        tableroDatos[i][pos - 1] *= 2;
                        score=score+tableroDatos[i][pos - 1];
                        tableroDatos[i][j] = 0;
                        if(tableroDatos[i][pos - 1]==2048){
                            game.setWin(true);
                        }
                        swap = true;
                    } else {
                        if (pos != j) {
                            tableroDatos[i][pos] = tableroDatos[i][j];
                            tableroDatos[i][j] = 0;
                            swap = true;
                        }
                        pos++;
                    }
                }
            }
        }
    }

    private void moverDerecha() {
        for (int i = 0; i < rows; i++) {
            int pos = cols - 1; // Posición para insertar números no cero
            for (int j = cols - 1; j >= 0; j--) {
                if (tableroDatos[i][j] != 0) {
                    if (pos < cols - 1 && tableroDatos[i][pos + 1] == tableroDatos[i][j]) {
                        tableroDatos[i][pos + 1] *= 2;
                        score=score+tableroDatos[i][pos + 1];
                        tableroDatos[i][j] = 0;
                        swap = true;
                        if(tableroDatos[i][pos - 1]==2048){
                            game.setWin(true);
                        }
                    } else {
                        if (pos != j) {
                            tableroDatos[i][pos] = tableroDatos[i][j];
                            tableroDatos[i][j] = 0;
                            swap = true;
                        }
                        pos--;
                    }
                }
            }
        }
    }

    private void moverArriba() {
        for (int j = 0; j < cols; j++) {
            int pos = 0; // Posición para insertar números no cero
            for (int i = 0; i < rows; i++) {
                if (tableroDatos[i][j] != 0) {
                    if (pos > 0 && tableroDatos[pos - 1][j] == tableroDatos[i][j]) {
                        tableroDatos[pos - 1][j] *= 2;
                        score=score+tableroDatos[pos - 1][j];
                        tableroDatos[i][j] = 0;
                        swap = true;
                        if(tableroDatos[i][pos - 1]==2048){
                            game.setWin(true);
                        }
                    } else {
                        if (pos != i) {
                            tableroDatos[pos][j] = tableroDatos[i][j];
                            tableroDatos[i][j] = 0;
                            swap = true;
                        }
                        pos++;
                    }
                }
            }
        }
    }

    private void moverAbajo() {
        for (int j = 0; j < cols; j++) {
            int pos = rows - 1; // Posición para insertar números no cero
            for (int i = rows - 1; i >= 0; i--) {
                if (tableroDatos[i][j] != 0) {
                    if (pos < rows - 1 && tableroDatos[pos + 1][j] == tableroDatos[i][j]) {
                        tableroDatos[pos + 1][j] *= 2;
                        score=score+tableroDatos[pos + 1][j];
                        tableroDatos[i][j] = 0;
                        swap = true;
                        if(tableroDatos[i][pos - 1]==2048){
                            game.setWin(true);
                        }
                    } else {
                        if (pos != i) {
                            tableroDatos[pos][j] = tableroDatos[i][j];
                            tableroDatos[i][j] = 0;
                            swap = true;
                        }
                        pos--;
                    }
                }
            }
        }
    }

    public void goBack(){
        tableroDatos=copyTablero(previusTablero);
        score=getLastScore();
        Log.d("Tablero", "Tablero retrocedido");
    }

    public boolean hasPerdido() {
        // Verificar si hay celdas vacías
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tableroDatos[i][j] == 0) {
                    return false; // Hay espacio vacío, el juego no ha terminado
                }
            }
        }

        // Verificar si hay celdas adyacentes con el mismo valor para permitir fusión
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i < rows - 1 && tableroDatos[i][j] == tableroDatos[i + 1][j]) {
                    return false; // Se puede fusionar verticalmente
                }
                if (j < cols - 1 && tableroDatos[i][j] == tableroDatos[i][j + 1]) {
                    return false; // Se puede fusionar horizontalmente
                }
            }
        }

        // Si no hay espacio vacío ni movimientos posibles, el juego ha terminado
        return true;
    }
//------------------------------------test--------fcuntions


    public void setupPerder() {
        int[][] patron = {
                {0, 4, 8, 16},
                {32, 64, 128, 256},
                {512, 1024, 2, 4},
                {8, 16, 32, 64}
        };

        // Copiamos el patrón en el tablero
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tableroDatos[i][j] = patron[i][j];
            }
        }

        Log.d("Tablero", "Tablero preparado para la derrota en el próximo movimiento.");
    }

    public void setupGanar() {
        // Llena el tablero con valores para que una combinación logre 2048 en un solo movimiento
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tableroDatos[i][j] = 2; // Llenamos el tablero con valores bajos para no afectar la jugada clave
            }
        }

        // Situamos dos fichas de 1024 juntas para ganar en un solo movimiento
        tableroDatos[0][0] = 1024;
        tableroDatos[0][1] = 1024;

        Log.d("Tablero", "Tablero preparado para ganar en el próximo movimiento.");
    }
}
