package com.example.gamecenter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class game_buscaminas extends AppCompatActivity {

    Button b_easy;
    Button b_dificil;
    Button b_getLucky;
    Button b_volver;
    String usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_buscaminas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usuario=getIntent().getStringExtra("usuario");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonId = getResources().getResourceEntryName(v.getId());
                Intent intent=new Intent(game_buscaminas.this,BuscaminasTablero.class);
                switch (buttonId) {
                    case "b_facil":
                        intent.putExtra("bombas",10);
                        intent.putExtra("tablero",9);



                        break;
                    case "b_dificil":
                        // Acción para el botón "b_dificil"
                        intent.putExtra("bombas",40);
                        intent.putExtra("tablero",16);

                        break;
                    case "b_getLucky":
                        // Acción para el botón "b_getLucky"
                        intent.putExtra("bombas",80);
                        intent.putExtra("tablero",9);

                        break;

                    // Acción por defecto
                }
                intent.putExtra("username", usuario);
                startActivity(intent);
            }
        };
        b_easy = findViewById(R.id.b_facil);
        b_dificil = findViewById(R.id.b_dificil);
        b_getLucky = findViewById(R.id.b_getLucky);
        b_easy.setOnClickListener(listener);
        b_dificil.setOnClickListener(listener);
        b_getLucky.setOnClickListener(listener);
        b_volver = findViewById(R.id.b_volver);
        b_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(game_buscaminas.this, home_screen.class);
                intent.putExtra("username", usuario);
                startActivity(intent);
                finish();
            }
        });


    }


}
