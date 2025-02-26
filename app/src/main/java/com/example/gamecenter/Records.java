package com.example.gamecenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Records extends AppCompatActivity {

    Spinner spinner_juegos;
    Button b_mostrar;
    Button b_volver;
    TableLayout tabla_records;
    DBhelper dBhelper;
    String usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.usuario = getIntent().getStringExtra("usuario");
        dBhelper = new DBhelper(this);
        spinner_juegos = findViewById(R.id.spinner);
        b_mostrar = findViewById(R.id.button3);
        tabla_records = findViewById(R.id.tabla_records);

        DBhelper dbHelper = new DBhelper(this);
        ArrayList<Juego> listaJuegos = dBhelper.getListaJuegos();

        ArrayAdapter<Juego> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaJuegos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_juegos.setAdapter(adapter);




        b_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Juego juegoSeleccionado = (Juego) spinner_juegos.getSelectedItem();
                int idJuego = (int) juegoSeleccionado.getId();
                llenarTablaConRecords(idJuego);
                Toast.makeText(Records.this, "ID del juego seleccionado: " + idJuego, Toast.LENGTH_SHORT).show();
            }
        });


        b_volver=findViewById(R.id.b_volver);
        b_volver.setOnClickListener(v -> {
            Intent intent = new Intent(Records.this, home_screen.class);
            intent.putExtra("username", usuario);
            startActivity(intent);
            finish();
        });


    }

    public void llenarTablaConRecords(int idJuego) {
        // Obtener la lista de registros para el juego seleccionado
        ArrayList<Record> listaRecords = dBhelper.getRecordsPorJuego(idJuego);

        // Referencia a la TableLayout donde agregaremos las filas
        TableLayout tablaRecords = findViewById(R.id.tabla_records);

        // Limpiar la tabla antes de llenarla
        tablaRecords.removeAllViews();

        // Recorrer los registros y agregar filas a la tabla
        for (Record record : listaRecords) {
            // Crear una nueva fila
            TableRow tableRow = new TableRow(this);

            // Crear TextView para el nombre del usuario
            TextView textViewUsuario = new TextView(this);
            textViewUsuario.setText(record.getNombreUsuario());
            textViewUsuario.setPadding(8, 8, 8, 8);
            textViewUsuario.setTextSize(18);
            textViewUsuario.setTextColor(Color.BLACK);
            Typeface typeface = Typeface.create("casual", Typeface.NORMAL);
            textViewUsuario.setTypeface(typeface);
            textViewUsuario.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewUsuario.setBackgroundColor(Color.parseColor("#695c4f"));
            textViewUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            // Crear TextView para el record del usuario
            TextView textViewRecord = new TextView(this);
            if(idJuego==2){
                // Convierte el tiempo en segundos a minutos y segundos
                int minutes = record.getPuntuacion() / 60;
                int seconds = record.getPuntuacion() % 60;

                // Formatea el texto como MM:SS
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                // Muestra el tiempo formateado
                textViewRecord.setText(timeFormatted);
            }else{
                textViewRecord.setText(String.valueOf(record.getPuntuacion()));
            }
            textViewRecord.setTextSize(18);
            textViewRecord.setTextColor(Color.BLACK);
            textViewRecord.setTypeface(typeface);
            textViewRecord.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewRecord.setPadding(8, 8, 8, 8);
            textViewRecord.setBackgroundColor(Color.parseColor("#695c4f"));
            textViewRecord.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            // Agregar los TextViews a la fila
            tableRow.addView(textViewUsuario);
            tableRow.addView(textViewRecord);

            // Agregar la fila a la tabla
            tablaRecords.addView(tableRow);
        }
    }

}