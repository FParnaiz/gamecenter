package com.example.gamecenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.stream.Stream;

public class Ajustes extends AppCompatActivity {

    DBhelper dbhelper;
    String usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ajustes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usuario=getIntent().getStringExtra("username");

        Button b_borrarRecords = findViewById(R.id.b_borrarRecords);
        b_borrarRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper = new DBhelper(Ajustes.this);
                dbhelper.borrarRecords(usuario);
            }
        });












        ImageView rueda_atras = findViewById(R.id.rueda_atras);
        ImageView rueda_delante = findViewById(R.id.rueda_delante);
        ImageView humo1 = findViewById(R.id.humo1);
        ImageView humo2 = findViewById(R.id.humo2);
        ImageView humo3 = findViewById(R.id.humo3);

        // Load animations
        Animation anim_rueda_atras = AnimationUtils.loadAnimation(this, R.anim.rueda_atras);
        Animation anim_rueda_delante = AnimationUtils.loadAnimation(this, R.anim.rueda_delante);
        Animation anim_humo1 = AnimationUtils.loadAnimation(this, R.anim.humo1);
        Animation anim_humo2 = AnimationUtils.loadAnimation(this, R.anim.humo2);
        Animation anim_humo3 = AnimationUtils.loadAnimation(this, R.anim.humo3);

        // Set up a Handler to restart animations
        Handler handler = new Handler();

        // Start the animations
        rueda_atras.startAnimation(anim_rueda_atras);
        rueda_delante.startAnimation(anim_rueda_delante);
        humo1.startAnimation(anim_humo1);
        humo2.startAnimation(anim_humo2);
        humo3.startAnimation(anim_humo3);

        // Handler to restart animations after a delay (animation duration + some margin)
        anim_humo3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // You can add any action needed on start
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // When humo3's animation ends, restart all animations
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        humo1.startAnimation(anim_humo1);
                        humo2.startAnimation(anim_humo2);
                        humo3.startAnimation(anim_humo3);
                    }
                }, 0); // Immediate restart after animation ends
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // You can add any action needed on repeat (optional)
            }
        });
    }





}