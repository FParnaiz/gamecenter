package com.example.gamecenter;

import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

public class TableroDatos {
    private int[][] tableroDatos; // Matriz del tablero
    private final int rows;
    private final int cols;
    private boolean swap = false;

    public TableroDatos(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tableroDatos = new int[rows][cols];
        Log.d("Tablero", "Tablero inicializado");
    }

    public int[][] getmatriz() {
        return tableroDatos;
    }

    public int[][] generarTablero() {
        Log.d("Tablero", "Generando tablero");
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tableroDatos[i][j] = 0;
            }
        }
        tableroDatos[random.nextInt(rows)][random.nextInt(cols)] = 2;
        return tableroDatos;
    }

    public int[][] actualizarTablero() {
        Random random = new Random();
        ArrayList<int[]> vacios = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tableroDatos[i][j] == 0) {
                    vacios.add(new int[]{i, j});
                }
            }
        }

        if (!vacios.isEmpty()) {
            int[] pos = vacios.get(random.nextInt(vacios.size()));
            tableroDatos[pos[0]][pos[1]] = (random.nextInt(4) == 0) ? 4 : 2;
        } else {
            Log.d("Tablero", "No hay espacios vacíos");
        }
        return tableroDatos;
    }

    public int[][] moverCasillas(String direccion) {
        swap = false; // Reinicia el estado de swap
        switch (direccion) {
            case "a": moverIzquierda(); break;
            case "d": moverDerecha(); break;
            case "w": moverArriba(); break;
            case "s": moverAbajo(); break;
        }
        if (swap) {
            actualizarTablero();
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
                        tableroDatos[i][j] = 0;
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
                        tableroDatos[i][j] = 0;
                        swap = true;
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
                        tableroDatos[i][j] = 0;
                        swap = true;
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
                        tableroDatos[i][j] = 0;
                        swap = true;
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
}
