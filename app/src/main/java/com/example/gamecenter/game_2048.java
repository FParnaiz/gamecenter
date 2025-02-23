package com.example.gamecenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    private MapaCasillas mapaCasillas;
    private DBhelper dbhelper;
    private String nombreUsuario;
    private String juego="2048";
    private Boolean win = false;
    private Boolean endless = false;
    private TextView t_record;



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
        this.dbhelper = new DBhelper(this);
        this.nombreUsuario = getIntent().getStringExtra("usuario");
        t_record = findViewById(R.id.t_Record);
        t_record.setText(String.valueOf(dbhelper.getRecord(nombreUsuario, juego)));

        mapaCasillas = new MapaCasillas();
        gestos_2024 gestureListener = new gestos_2024(this);
        gestureDetector = new GestureDetector(this, gestureListener);
        tableroDatos = new TableroDatos(ROWS, COLS,this);
        tableroDatos.generarTablero();
        TextView puntos = findViewById(R.id.t_Puntuacion);
        tableroVisual = new TableroVisual(ROWS, COLS,tableroDatos,puntos,this);
        tableroVisual.dibujarTablero();

        Button b_back=findViewById(R.id.b_back);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tablero", "Boton back pulsado");
                tableroDatos.goBack();
                tableroVisual.setTableroDatos(tableroDatos);
                tableroVisual.dibujarTablero();
            }
        });

        Button b_perder=findViewById(R.id.b_perder);
        b_perder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Tablero", "Boton perder pulsado");
                tableroDatos.setupPerder();
                tableroVisual.setTableroDatos(tableroDatos);
                tableroVisual.dibujarTablero();
            }
            });
        Button b_ganar=findViewById(R.id.b_ganar);
        b_ganar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("Tablero", "Boton ganar pulsado");
                tableroDatos.setupGanar();
                tableroVisual.setTableroDatos(tableroDatos);
                tableroVisual.dibujarTablero();
                }
            });

        Button b_volver=findViewById(R.id.b_volver);
        b_volver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                guardarPuntuacion(tableroDatos.getScore());
                Intent intent = new Intent(game_2048.this, home_screen.class);
                String usuario = getIntent().getStringExtra("usuario");
                intent.putExtra("username", usuario);
                startActivity(intent);
                finish();

            }
        });





    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass touch events to the GestureDetector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public void perder(int puntuacion) {

        if (guardarPuntuacion(puntuacion)) {
            Log.d("Puntuacion", "Puntuación guardada exitosamente.");
            mostrarAlerta("New Record", "Seguir jugando");

        } else {
            Log.d("Puntuacion", "Error al guardar la puntuación.");
            mostrarAlerta("Has Perdido", "Seguir jugando");
        }
    }

    public Boolean guardarPuntuacion(int puntuacion) {
        // Registro de los valores antes de guardar
        Log.d("Puntuacion", "Guardando puntuación...");
        Log.d("Puntuacion", "Nombre del usuario: " + nombreUsuario);
        Log.d("Puntuacion", "Puntuación actual: " + puntuacion);
        Log.d("Puntuacion", "Juego: " + juego);

        // Llamada al método para guardar en la base de datos
        boolean resultado = dbhelper.guardarPuntuacion(nombreUsuario, puntuacion, juego);
        return resultado;

        // Registro del resultado de la operación

    }

    public void ganar() {

        mostrarAlertaVictoria("Has Ganado", "Seguir jugando");
    }

    public void reiniciarJuego() {

        tableroDatos = new TableroDatos(ROWS, COLS,this);
        tableroDatos.generarTablero();
        TextView puntos = findViewById(R.id.t_Puntuacion);
        tableroVisual = new TableroVisual(ROWS, COLS,tableroDatos,puntos,this);
        tableroVisual.dibujarTablero();

        t_record.setText(String.valueOf(dbhelper.getRecord(nombreUsuario, juego)));
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)  // Título de la alerta
                .setMessage(mensaje) // Mensaje de la alerta
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reiniciarJuego();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en "Cancelar"
                        Intent intent = new Intent(game_2048.this, home_screen.class);
                        String usuario = getIntent().getStringExtra("usuario");
                        intent.putExtra("username", usuario);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false); // Evita que el diálogo se cierre si se toca fuera de él

        // Mostrar el diálogo
        builder.create().show();
    }


    public void mostrarAlertaVictoria(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)  // Título de la alerta
                .setMessage(mensaje) // Mensaje de la alerta
                .setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guardarPuntuacion(tableroDatos.getScore());
                        reiniciarJuego();

                    }
                })
                .setNegativeButton("Seguir jugando", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guardarPuntuacion(tableroDatos.getScore());
                        t_record.setText(String.valueOf(dbhelper.getRecord(nombreUsuario, juego)));
                        endless=true;
                    }
                })
                .setCancelable(false); // Evita que el diálogo se cierre si se toca fuera de él

        // Mostrar el diálogo
        builder.create().show();
    }


    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }

    public Boolean getEndless() {
        return endless;
    }

    public void setEndless(Boolean playing) {
        this.endless = playing;
    }
}