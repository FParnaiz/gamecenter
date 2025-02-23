package com.example.gamecenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        usuario = getIntent().getStringExtra("usuario");
        Log.d("Ajustes", "Usuario recibido: " + usuario);

        dbhelper = new DBhelper(Ajustes.this);

        Button b_borrarRecords = findViewById(R.id.b_borrarRecords);
        b_borrarRecords.setOnClickListener(v -> {
            boolean deleted = dbhelper.borrarRecords(usuario);
            if (deleted) {
                Log.d("DBhelper", "Records eliminados correctamente");
                mostrarMensaje("Los records han sido eliminados.");

            } else {
                Log.d("DBhelper", "No se encontraron records para eliminar");
                mostrarMensaje("No hay records para eliminar.");
            }
        });

        Button b_cambiarNombre = findViewById(R.id.b_cambiarNombre);
        b_cambiarNombre.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Ajustes.this);
            builder.setTitle("Cambiar Nombre de Usuario");

            final EditText input = new EditText(Ajustes.this);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String nuevoNombre = input.getText().toString().trim();
                Log.d("Ajustes", "Nuevo nombre ingresado: " + nuevoNombre);

                if (!nuevoNombre.isEmpty()) {
                    dbhelper = new DBhelper(Ajustes.this);
                    boolean cambiado = dbhelper.cambiarNombreUsuario(usuario, nuevoNombre);
                    dbhelper.close();

                    if (cambiado) {
                        usuario = nuevoNombre; // Actualizar variable local
                        mostrarMensaje("Éxito", "Tu nombre de usuario ha sido cambiado correctamente.");
                    } else {
                        mostrarMensaje("Error", "No se pudo cambiar el nombre. Puede que ya exista otro usuario con ese nombre.");
                    }
                } else {
                    mostrarMensaje("Error", "El nuevo nombre de usuario no puede estar vacío.");
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });



        Button b_cambiarContraseña = findViewById(R.id.b_cambiarPassword);
        b_cambiarContraseña.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Ajustes.this);
            builder.setTitle("Cambiar Contraseña");

            // Crear un LinearLayout para organizar los campos de entrada verticalmente
            LinearLayout layout = new LinearLayout(Ajustes.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Crear los EditText para la contraseña actual y nueva
            final EditText inputActual = new EditText(Ajustes.this);
            inputActual.setHint("Contraseña actual");
            inputActual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            final EditText inputNueva = new EditText(Ajustes.this);
            inputNueva.setHint("Nueva contraseña");
            inputNueva.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            // Añadir los EditText al LinearLayout
            layout.addView(inputActual);
            layout.addView(inputNueva);

            // Configurar el diálogo
            builder.setView(layout);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String contraseñaActual = inputActual.getText().toString().trim();
                String nuevaContraseña = inputNueva.getText().toString().trim();

                if (!contraseñaActual.isEmpty() && !nuevaContraseña.isEmpty()) {
                    boolean contraseñaCorrecta = dbhelper.checkPassword(usuario, contraseñaActual);

                    if (contraseñaCorrecta) {
                        boolean cambiado = dbhelper.cambiarContraseña(usuario, nuevaContraseña);
                        if (cambiado) {
                            mostrarMensaje("Éxito", "La contraseña ha sido cambiada correctamente.");
                        } else {
                            mostrarMensaje("Error", "No se pudo cambiar la contraseña. Intenta nuevamente.");
                        }
                    } else {
                        mostrarMensaje("Error", "La contraseña actual es incorrecta.");
                    }
                } else {
                    mostrarMensaje("Error", "Por favor, ingrese ambos campos.");
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        Button b_atras = findViewById(R.id.b_atras);
        b_atras.setOnClickListener(v -> {
            Intent intent = new Intent(Ajustes.this, home_screen.class);
            intent.putExtra("username", usuario);
            startActivity(intent);
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

    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        new AlertDialog.Builder(Ajustes.this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }






}