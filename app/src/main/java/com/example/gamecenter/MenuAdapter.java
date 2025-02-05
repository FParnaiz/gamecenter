package com.example.gamecenter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenuAdapter extends FragmentStateAdapter {

    private final InfoMenu infoMenu;
    private final String usuario;

    public MenuAdapter(@NonNull FragmentActivity activity, InfoMenu infoMenu, String usuario) {
        super(activity);
        this.infoMenu = infoMenu;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


            InfoJuego juego = infoMenu.getJuegos().get(position);
            return MenuFragment.newInstance(juego.getTitulo(), juego.getRecord(), juego.getActivity(), usuario);

    }

    @Override
    public int getItemCount() {
        return infoMenu.getJuegos().size();
    }
}
