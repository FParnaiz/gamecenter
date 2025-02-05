package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class home_screen extends AppCompatActivity {

    private ViewPager2 viewPager;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Intent intent = getIntent();
        String usuario = intent.getStringExtra("username");

        viewPager = findViewById(R.id.viewPager);

        // Crear la lista de juegos dinámicamente

        // Crear el objeto InfoMenu
        InfoMenu infoMenu = new InfoMenu(usuario, this);
        Log.d("CustomDialog", "Nombre "+usuario);
        // Configurar el adaptador con el objeto InfoMenu
        adapter = new MenuAdapter(this, infoMenu, usuario);
        viewPager.setAdapter(adapter);
    }


}
