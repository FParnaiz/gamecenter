package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void goTo2048(View view) {
        Intent intent = new Intent(this, game_2048.class);
        startActivity(intent);
    }
    public void goToMines(View view) {
        Intent intent = new Intent(this, game_Mines.class);
        startActivity(intent);
    }

}
