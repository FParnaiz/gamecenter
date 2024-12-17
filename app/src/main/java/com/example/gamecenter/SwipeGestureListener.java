package com.example.gamecenter;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private MainActivity activity;

    public SwipeGestureListener(MainActivity activity) {
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
            activity.tableroVisual.dibujarTablero(activity, activity.tableroDatos, R.drawable.ficha);
            return true;
        }

        // Detect swipe left (horizontal swipe)
        if (Math.abs(deltaX) > Math.abs(deltaY) && deltaX < -swipeThreshold) {
            // Swipe left: call moverIzquierda
            activity.tableroDatos.moverCasillas("a");
            activity.tableroVisual.dibujarTablero(activity, activity.tableroDatos, R.drawable.ficha);
            return true;
        }

        // Detect swipe down (vertical swipe)
        if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY > swipeThreshold) {
            // Swipe down: call moverAbajo
            activity.tableroDatos.moverCasillas("s");
            activity.tableroVisual.dibujarTablero(activity, activity.tableroDatos, R.drawable.ficha);
            return true;
        }

        // Detect swipe up (vertical swipe)
        if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY < -swipeThreshold) {
            // Swipe up: call moverArriba
            activity.tableroDatos.moverCasillas("w");
            activity.tableroVisual.dibujarTablero(activity, activity.tableroDatos, R.drawable.ficha);
            return true;
        }

        // If no swipe gesture is detected, return false
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
