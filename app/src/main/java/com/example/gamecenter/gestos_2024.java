package com.example.gamecenter;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class gestos_2024 extends GestureDetector.SimpleOnGestureListener {
    private game_2048 activity;

    public gestos_2024(game_2048 activity) {
        this.activity = activity;
    }

    // Override onFling to detect swipe gestures
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - e1.getX();
        float deltaY = e2.getY() - e1.getY();

        // Swipe threshold
        int swipeThreshold = 100;

        // Detect swipe right (horizontal swipe)
        if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX > swipeThreshold) {
            // Swipe right: call moverDerecha
            activity.tableroDatos.moverCasillas("d");
            activity.tableroVisual.dibujarTablero();
            return true;
        }

        // Detect swipe left (horizontal swipe)
        if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX < -swipeThreshold) {
            // Swipe left: call moverIzquierda
            activity.tableroDatos.moverCasillas("a");
            activity.tableroVisual.dibujarTablero();
            return true;
        }

        // Detect swipe down (vertical swipe)
        if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY > swipeThreshold) {
            // Swipe down: call moverAbajo
            activity.tableroDatos.moverCasillas("s");
            activity.tableroVisual.dibujarTablero();
            return true;
        }

        // Detect swipe up (vertical swipe)
        if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY < -swipeThreshold) {
            // Swipe up: call moverArriba
            activity.tableroDatos.moverCasillas("w");
            activity.tableroVisual.dibujarTablero();
            return true;
        }

        // If no swipe gesture is detected, return false
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
