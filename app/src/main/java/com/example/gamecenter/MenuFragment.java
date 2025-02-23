package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {


    private String titulo;
    private int record;
    private String activity;
    private String usuario;

    // Factory method to create a new instance of this fragment
    public static MenuFragment newInstance(String titulo, int record, String activity, String usuario) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putInt("record", record);
        args.putString("activity", activity);
        args.putString("usuario", usuario);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        titulo = getArguments().getString("titulo");
        record = getArguments().getInt("record");
        activity = getArguments().getString("activity");
        Log.d("MenuFragment", "onCreateView: titulo: " + titulo + " record: " + record + " activity: " + activity);
        usuario = getArguments().getString("usuario");
        if(titulo.equals("Ajustes")){
            TextView tituloTextView = rootView.findViewById(R.id.tituloMenu);
            tituloTextView.setText(titulo);
            ImageView imagenMenu = rootView.findViewById(R.id.imagenMenu);
            imagenMenu.setImageResource(R.drawable.ajustes);
            TextView recordTextView = rootView.findViewById(R.id.recordMenu);
            recordTextView.setText("");
            FrameLayout framemenu= rootView.findViewById(R.id.frameMenu);
            framemenu.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), Ajustes.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            });


        }else{
            TextView tituloTextView = rootView.findViewById(R.id.tituloMenu);
            tituloTextView.setText(titulo);
            TextView recordTextView = rootView.findViewById(R.id.recordMenu);
            if(titulo.equals("Buscaminas")){
                int minutes = record / 60;
                int seconds = record % 60;

                // Formatear el texto como MM:SS
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                recordTextView.setText("Record: "+timeFormatted);

            }else {
                recordTextView.setText("Record: "+String.valueOf(record));
            };

            ImageView imagenMenu = rootView.findViewById(R.id.imagenMenu);
            String nombreImagen = "j_"+titulo.toLowerCase().replace(" ", "_");
            int resID = getResources().getIdentifier(nombreImagen, "drawable", getActivity().getPackageName());
            imagenMenu.setImageResource(resID);
            FrameLayout framemenu= rootView.findViewById(R.id.frameMenu);
            framemenu.setOnClickListener(v-> {
                try {
                    Log.d("MenuFragment", "onClick: Clicked on " + activity);
                    Class<?> targetActivity = Class.forName("com.example.gamecenter." + activity);
                    Intent intent = new Intent(getActivity(), targetActivity);
                    intent.putExtra("usuario", usuario);
                    intent.putExtra("titulo", titulo);

                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }





        return rootView;
    }
}
