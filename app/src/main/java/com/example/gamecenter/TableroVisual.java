package com.example.gamecenter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TableroVisual {

    private TextView[][] tableroVisual; // Holds the TextViews for the visual grid
    private final int rows;
    private final int cols;

    public TableroVisual(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tableroVisual = new TextView[rows][cols];
    }

    public void dibujarTablero(Context context, TableroDatos tableroDatos, int drawableResId) {
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
                    tableroVisual[i][j].setText(String.valueOf(arrayTablero[i][j]));
                    tableroVisual[i][j].setBackgroundResource(drawableResId);
                    Log.d("Tablero", "Setting value " + arrayTablero[i][j] + " at " + i + ", " + j);
                }
            }
        }
    }



}
