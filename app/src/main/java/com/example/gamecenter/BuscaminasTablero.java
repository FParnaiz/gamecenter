package com.example.gamecenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class BuscaminasTablero extends AppCompatActivity {

    int tablero_size;
    int numero_bombas;
    buscaminas_datos datos;
    mapa_minas mapa;
    HashMap<Integer, String> posicionesBotones;
    HashMap<String, ImageButton> botonesTablero;
    Set<String> casillasDestapadas;
    boolean juegoTerminado;
    TextView t_bombas;
    String usuario;
    private int secondsPassed = 0;
    private Handler handler;
    TextView t_tiempo;
    boolean playing=false;
    DBhelper dbhelper;
    GridLayout tablero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscaminas_tablero);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar variables
        tablero_size = getIntent().getIntExtra("tablero", 0);
        numero_bombas = getIntent().getIntExtra("bombas", 0);
        datos = new buscaminas_datos(tablero_size, numero_bombas);
        mapa = new mapa_minas();
        posicionesBotones = new HashMap<>();
        botonesTablero = new HashMap<>();
        casillasDestapadas = new HashSet<>();
        juegoTerminado = false;
        t_bombas = findViewById(R.id.t_bombas);
        t_bombas.setText(String.format("%04d", numero_bombas));
        usuario = getIntent().getStringExtra("username");
        Log.d("BuscaminasTablero", "Usuario: " + usuario);
        handler = new Handler(Looper.getMainLooper());
        t_tiempo=findViewById(R.id.t_tiempo);
        dbhelper = new DBhelper(this);

        tablero = findViewById(R.id.tablero);
        tablero.setColumnCount(tablero_size);
        tablero.setRowCount(tablero_size);

        crearTablerovisual();



        Button b_volver = findViewById(R.id.b_volver);
        b_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuscaminasTablero.this, game_buscaminas.class);
                intent.putExtra("usuario", usuario);

                startActivity(intent);
            }
        });


    }

    private void crearTablerovisual(){
        for (int i = 0; i < tablero_size; i++) {
            for (int j = 0; j < tablero_size; j++) {
                ImageButton button = new ImageButton(this);
                String nombre = i + "-" + j;
                int id = View.generateViewId();
                button.setId(id);
                posicionesBotones.put(id, nombre);
                botonesTablero.put(nombre, button);
                button.setImageResource(R.drawable.base_tile);
                button.setScaleType(ImageView.ScaleType.FIT_XY);
                button.setPadding(0, 0, 0, 0);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                params.setMargins(0, 0, 0, 0);
                button.setLayoutParams(params);

                button.setOnClickListener(v -> destaparCasilla(v.getId()));
                button.setOnLongClickListener(v -> marcarBandera(v));

                tablero.addView(button);
            }
        }
    }

    private void destaparCasilla(int idBoton) {
        if (juegoTerminado) return;
        if (playing==false){
            playing=true;
            handler.postDelayed(timerRunnable, 1000);
        }

        String posicion = posicionesBotones.get(idBoton);
        if (casillasDestapadas.contains(posicion)) return;

        int valor = datos.getValor(posicion);
        ImageButton boton = botonesTablero.get(posicion);

        // Destapar la casilla actual
        casillasDestapadas.add(posicion);
        boton.setImageResource(mapa.getFicha(valor));

        // Si es una bomba, fin del juego
        if (valor == -1) {
            juegoTerminado = true;
            mostrarTodasLasBombas();
            mostrarAlertaDerrota("Has perdido", "Volver a jugar");
            playing=false;
            handler.removeCallbacks(timerRunnable);
            return;
        }

        // Si es un espacio vacío (valor 0), destapar casillas adyacentes
        if (valor == 0) {
            destaparAdyacentes(posicion);
        }

        // Verificar si ha ganado
        verificarVictoria();
    }

    private void destaparAdyacentes(String posicion) {
        String[] coords = posicion.split("-");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        // Recorrer las 8 casillas adyacentes
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int newRow = row + i;
                int newCol = col + j;

                // Verificar límites del tablero
                if (newRow >= 0 && newRow < tablero_size &&
                        newCol >= 0 && newCol < tablero_size) {

                    String nuevaPosicion = newRow + "-" + newCol;
                    ImageButton botonAdyacente = botonesTablero.get(nuevaPosicion);

                    if (botonAdyacente != null && !casillasDestapadas.contains(nuevaPosicion)) {
                        destaparCasilla(botonAdyacente.getId());
                    }
                }
            }
        }
    }

    private boolean marcarBandera(View v) {
        if (!juegoTerminado) {
            ImageButton imageButton = (ImageButton) v;
            String posicion = posicionesBotones.get(v.getId());

            if (!casillasDestapadas.contains(posicion)) {
                imageButton.setImageResource(R.drawable.flag);
                casillasDestapadas.add(posicion);
                numero_bombas--;
                t_bombas.setText(String.format("%04d", numero_bombas));

            } else {
                imageButton.setImageResource(R.drawable.base_tile);
                casillasDestapadas.remove(posicion);
                numero_bombas++;
                t_bombas.setText(String.format("%04d", numero_bombas));
            }
        }
        return true;
    }

    private void mostrarTodasLasBombas() {
        for (int i = 0; i < tablero_size; i++) {
            for (int j = 0; j < tablero_size; j++) {
                String pos = i + "-" + j;
                if (datos.getValor(pos) == -1) {
                    botonesTablero.get(pos).setImageResource(R.drawable.mine);
                }
            }
        }
    }

    private void verificarVictoria() {
        int casillasSeguras = (tablero_size * tablero_size) - numero_bombas;
        if (casillasDestapadas.size() == casillasSeguras) {
            juegoTerminado = true;
            Toast.makeText(this, "¡Has ganado!", Toast.LENGTH_LONG).show();
            playing=false;
            handler.removeCallbacks(timerRunnable);
            mostrarTodasLasBombas();
            if (guardarPuntuacion(secondsPassed)) {
                mostrarAlertaVictoria("Has Ganado", "Nuevo Record");
            }else {
                mostrarAlertaVictoria("Has Ganado", "No es nuevo Record");
            };
        }
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (playing) {
                secondsPassed++;

                // Convertir segundos a minutos y segundos
                int minutes = secondsPassed / 60;
                int seconds = secondsPassed % 60;

                // Formatear el texto como MM:SS
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                t_tiempo.setText(timeFormatted);

                handler.postDelayed(this, 1000); // Repetir cada segundo
            }
        }
    };

    public void reiniciarJuego() {

        tablero_size = getIntent().getIntExtra("tablero", 0);
        numero_bombas = getIntent().getIntExtra("bombas", 0);
        datos = new buscaminas_datos(tablero_size, numero_bombas);
        posicionesBotones = new HashMap<>();
        botonesTablero = new HashMap<>();
        casillasDestapadas = new HashSet<>();

        juegoTerminado = false;
        t_bombas = findViewById(R.id.t_bombas);
        t_bombas.setText(String.format("%04d", numero_bombas));
        secondsPassed = 0;
        crearTablerovisual();
        t_tiempo.setText("00:00");
    }

    public void mostrarAlertaDerrota(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)  // Título de la alerta
                .setMessage(mensaje) // Mensaje de la alerta
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reiniciarJuego();

                    }
                })
                .setNegativeButton("Cambiar dificultad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BuscaminasTablero.this, game_buscaminas.class);
                        intent.putExtra("usuario", usuario);
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
                .setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        reiniciarJuego();

                    }
                })
                .setNegativeButton("Cambiar Dificultad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BuscaminasTablero.this, game_buscaminas.class);
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false); // Evita que el diálogo se cierre si se toca fuera de él

        // Mostrar el diálogo
        builder.create().show();
    }

    public Boolean guardarPuntuacion(int segundos) {
        return dbhelper.guardarPuntuacion(usuario, segundos, "Buscaminas");
    }

}