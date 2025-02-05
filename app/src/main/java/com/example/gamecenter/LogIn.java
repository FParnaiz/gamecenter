package com.example.gamecenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogIn extends AppCompatActivity {

    Button b_login ;
    Button b_sigin ;
    EditText userInput ;
    EditText passwordInput ;
    DBhelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.log_in);

        // Set padding for the layout based on system bars insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find ImageViews
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
        dbhelper = new DBhelper(this);
        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        b_login = findViewById(R.id.b_login);
        b_sigin = findViewById(R.id.b_sigin);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userInput.getText().toString();
                String password = passwordInput.getText().toString();
                if(dbhelper.checkPassword(username, password)){
                    Intent intent = new Intent(LogIn.this, home_screen.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }else{
                    showInfoAlert("Error", "Usuario o contraseña incorrectos");
                };
            }

        });
        b_sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userInput.getText().toString();
                String password = passwordInput.getText().toString();
                if(dbhelper.checkUsuarioExist(username)){
                    showInfoAlert("Error", "El usuario ya existe");
                }else {
                    long code =dbhelper.insertUsuario(username,password);
                    if(code==-1){
                        showInfoAlert("Error", "El usuario ya existe");
                        return;
                    }
                    else {
                        Toast.makeText(LogIn.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this, home_screen.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });



    }

    public void showInfoAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when OK is clicked (optional)
                    }
                })
                .setCancelable(false)  // Set whether the dialog can be canceled by clicking outside
                .show();  // Show the dialog
    }

}
