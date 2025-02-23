package com.example.gamecenter;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TableroVisual {

    private TextView[][] tableroVisual; // Holds the TextViews for the visual grid
    private final int rows;
    private final int cols;
    private MapaCasillas mapaCasillas;
    private TableroDatos tableroDatos;
    private TextView puntos;
    private Context context;

    public TableroVisual(int rows, int cols, TableroDatos tableroDatos, TextView puntos,Context context) {
        this.rows = rows;
        this.cols = cols;
        this.tableroDatos = tableroDatos;
        mapaCasillas = new MapaCasillas();
        this.tableroVisual = new TextView[rows][cols];
        this.puntos = puntos;
        this.context=context;
    }
    public void setTableroDatos(TableroDatos tableroDatos) {
        this.tableroDatos = tableroDatos;
    }

    public void dibujarTablero() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Get the resource ID for the TextView
                String id = "sqr" + i + j;
                int resourceId = context.getResources().getIdentifier(id, "id", context.getPackageName());
                tableroVisual[i][j] = ((AppCompatActivity) context).findViewById(resourceId);
                int[][] arrayTablero=tableroDatos.getmatriz();
                tableroVisual[i][j].setText("");  // Elimina el texto
                tableroVisual[i][j].setBackground(null);
                if (arrayTablero[i][j] != 0) {
                    // Convert the integer value to a string

                    tableroVisual[i][j].setBackgroundResource(mapaCasillas.getFicha(arrayTablero[i][j]));
                    Log.d("Tablero", "Setting value " + arrayTablero[i][j] + " at " + i + ", " + j);
                }else{
                    tableroVisual[i][j].setBackgroundResource(mapaCasillas.getFicha(0));
                }
            }
        }
        puntos.setText(String.valueOf(tableroDatos.getScore()));
    }



}
