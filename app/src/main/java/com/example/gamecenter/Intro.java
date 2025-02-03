package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.intro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.intro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView rueda_atras = findViewById(R.id.rueda_atras);
        Animation anim_rueda_atras = AnimationUtils.loadAnimation(this, R.anim.rueda_atras);
        rueda_atras.startAnimation(anim_rueda_atras);
        ImageView rueda_delante = findViewById(R.id.rueda_delante);
        Animation anim_rueda_delante = AnimationUtils.loadAnimation(this, R.anim.rueda_delante);
        rueda_delante.startAnimation(anim_rueda_delante);
        ImageView humo1 = findViewById(R.id.humo1);
        Animation anim_humo1 = AnimationUtils.loadAnimation(this, R.anim.humo1);
        humo1.startAnimation(anim_humo1);
        ImageView humo2 = findViewById(R.id.humo2);
        Animation anim_humo2 = AnimationUtils.loadAnimation(this, R.anim.humo2);
        humo2.startAnimation(anim_humo2);
        ImageView humo3 = findViewById(R.id.humo3);
        Animation anim_humo3 = AnimationUtils.loadAnimation(this, R.anim.humo3);
        humo3.startAnimation(anim_humo3);
        TextView titulo = findViewById(R.id.titulo);
        Animation anim_titulo = AnimationUtils.loadAnimation(this, R.anim.titulo);
        titulo.startAnimation(anim_titulo);
        anim_humo3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(Intro.this,
                        home_screen.class));
                Intro.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}