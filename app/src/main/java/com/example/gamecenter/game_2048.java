package com.example.gamecenter;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class game_2048 extends AppCompatActivity {
    public TableroDatos tableroDatos;
    private GestureDetector gestureDetector;
    private static final int ROWS = 4; // Number of rows
    private static final int COLS = 4; // Number of columns
    public TableroVisual tableroVisual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_2048);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gestos_2024 gestureListener = new gestos_2024(this);
        gestureDetector = new GestureDetector(this, gestureListener);
        tableroDatos = new TableroDatos(ROWS, COLS);
        tableroDatos.generarTablero();
        tableroVisual = new TableroVisual(ROWS, COLS);
        tableroVisual.dibujarTablero(this, tableroDatos, R.drawable.ficha);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass touch events to the GestureDetector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
}