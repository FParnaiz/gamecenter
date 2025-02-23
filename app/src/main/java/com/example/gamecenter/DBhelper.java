package com.example.gamecenter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GameCenter.db";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBhelper", "onCreate:");
        db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL("CREATE TABLE usuarios (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre_usuario TEXT UNIQUE, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE juegos (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre_juego TEXT UNIQUE)");
        db.execSQL("CREATE TABLE records (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_juego INTEGER, id_usuario INTEGER, record INTEGER," +
                "FOREIGN KEY(id_juego) REFERENCES juegos(_id)," +
                "FOREIGN KEY(id_usuario) REFERENCES usuarios(_id))");

        db.execSQL("INSERT INTO juegos (nombre_juego) VALUES ('2048')");
        db.execSQL("INSERT INTO juegos (nombre_juego) VALUES ('Buscaminas')");
        db.execSQL("INSERT INTO usuarios (nombre_usuario, password) VALUES ('admin', 'admin')");
        db.execSQL("INSERT INTO records (id_juego, id_usuario, record) VALUES (1, 1, 100)");
        db.execSQL("INSERT INTO records (id_juego, id_usuario, record) VALUES (2, 1, 50)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertUsuario(String nombre, String password) {
        if (!checkUsuarioExist(nombre)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("nombre_usuario", nombre);
            contentValues.put("password", password);

            long result = db.insert("usuarios", null, contentValues);
            db.close(); // Cerrar base de datos
            return result;
        } else {
            return -1;
        }
    }

    public boolean guardarPuntuacion(String nombreUsuario, int puntuacion, String juego) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Obtener el ID del usuario
        String queryUsuario = "SELECT _id FROM usuarios WHERE nombre_usuario = ?";
        Log.d("DBhelper", "Buscando ID del usuario: " + nombreUsuario);
        Cursor cursorUsuario = db.rawQuery(queryUsuario, new String[]{nombreUsuario});

        if (!cursorUsuario.moveToFirst()) {
            Log.d("DBhelper", "El usuario no existe: " + nombreUsuario);
            cursorUsuario.close();
            db.close();
            return false;
        }

        int idUsuario = cursorUsuario.getInt(0);
        cursorUsuario.close();

        // Obtener el ID del juego
        String queryJuego = "SELECT _id FROM juegos WHERE nombre_juego = ?";
        Log.d("DBhelper", "Buscando ID del juego: " + juego);
        Cursor cursorJuego = db.rawQuery(queryJuego, new String[]{juego});

        if (!cursorJuego.moveToFirst()) {
            Log.d("DBhelper", "El juego no existe: " + juego);
            cursorJuego.close();
            db.close();
            return false;
        }

        int idJuego = cursorJuego.getInt(0);
        cursorJuego.close();

        // Verificar si el usuario ya tiene un récord para ese juego
        String queryRecord = "SELECT record FROM records WHERE id_usuario = ? AND id_juego = ?";
        Cursor cursorRecord = db.rawQuery(queryRecord, new String[]{String.valueOf(idUsuario), String.valueOf(idJuego)});

        boolean resultado = false;

        if (cursorRecord.moveToFirst()) {
            int recordActual = cursorRecord.getInt(0);
            Log.d("DBhelper", "Puntuación actual: " + recordActual + " | Nueva puntuación: " + puntuacion);
            cursorRecord.close();

            boolean debeActualizar = false;


            // - Para "Buscaminas": Se actualiza si la puntuación es menor.
            // - Para otros juegos: Se actualiza si la puntuación es mayor.
            if (juego.equals("Buscaminas") && puntuacion < recordActual) {
                Log.d("DBhelper", "Nuevo récord en Buscaminas (tiempo menor), actualizando...");
                debeActualizar = true;
            } else if (!juego.equals("Buscaminas") && puntuacion > recordActual) {
                Log.d("DBhelper", "Nuevo récord en otro juego (puntuación mayor), actualizando...");
                debeActualizar = true;
            }

            if (debeActualizar) {
                ContentValues values = new ContentValues();
                values.put("record", puntuacion);

                int filasAfectadas = db.update("records", values, "id_usuario = ? AND id_juego = ?",
                        new String[]{String.valueOf(idUsuario), String.valueOf(idJuego)});
                resultado = filasAfectadas > 0;
            }
        } else {
            cursorRecord.close();

            // Insertar nuevo récord si no existe
            Log.d("DBhelper", "Insertando nueva puntuación...");
            ContentValues values = new ContentValues();
            values.put("id_usuario", idUsuario);
            values.put("id_juego", idJuego);
            values.put("record", puntuacion);

            long insertResult = db.insert("records", null, values);
            resultado = insertResult != -1;
        }

        db.close();
        return resultado;
    }


    public boolean checkUsuarioExist(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?", new String[]{usuario});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        Log.d("DBhelper", "¿El usuario " + usuario + " existe?: " + exists);

        return exists;
    }



    public boolean checkPassword(String nombre, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombre});

        boolean isValid = false;

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
            isValid = storedPassword != null && storedPassword.equals(password);
        }

        cursor.close();
        db.close(); // Cerrar base de datos
        return isValid;
    }

    public ArrayList<InfoJuego> getJuegos(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (usuario == null) {
            Log.e("DBhelper", "getJuegos: usuario is null!");
            db.close(); // Cerrar base de datos
            return new ArrayList<>();
        }

        // Consulta para obtener todos los juegos y sus récords si existen
        String query = "SELECT juegos.nombre_juego, COALESCE(records.record, 0)"+"" +
                " AS record FROM juegos LEFT JOIN records ON records.id_juego = juegos._id"+
                " LEFT JOIN usuarios ON records.id_usuario = usuarios._id AND usuarios.nombre_usuario = ?";

        Cursor cursor = db.rawQuery(query, new String[]{usuario});
        ArrayList<InfoJuego> juegos = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("nombre_juego"));
                @SuppressLint("Range") int record = cursor.getInt(cursor.getColumnIndex("record"));
                Log.d("DBhelper", "Juego: " + titulo + ", Record: " + record);
                juegos.add(new InfoJuego(titulo, record, usuario));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close(); // Cerrar base de datos
        return juegos;
    }

    public boolean borrarRecords(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;

        String query = "SELECT _id FROM usuarios WHERE nombre_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") long userId = cursor.getLong(cursor.getColumnIndex("_id"));
            cursor.close();

            int rowsAffected = db.delete("records", "id_usuario = ?", new String[]{String.valueOf(userId)});
            result = rowsAffected > 0;
        } else {
            cursor.close();
        }

        db.close(); // Cerrar base de datos
        return result;
    }


    public boolean cambiarNombreUsuario(String usuarioActual, String nuevoUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DBhelper", "Intentando cambiar usuario: " + usuarioActual + " a " + nuevoUsuario);

        // Verificar si el nuevo nombre de usuario ya existe
        if (checkUsuarioExist(nuevoUsuario)) {
            Log.d("DBhelper", "El nuevo nombre de usuario ya existe en la BD.");
            db.close();
            return false;
        }

        // Actualizar el nombre de usuario
        ContentValues values = new ContentValues();
        values.put("nombre_usuario", nuevoUsuario);

        int filasAfectadas = db.update("usuarios", values, "nombre_usuario = ?", new String[]{usuarioActual});

        if (filasAfectadas > 0) {
            Log.d("DBhelper", "Nombre de usuario cambiado correctamente.");
        } else {
            Log.d("DBhelper", "No se encontró el usuario actual en la BD.");
        }

        db.close();
        return filasAfectadas > 0;
    }


    public boolean cambiarContraseña(String usuario, String nuevaContraseña) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Obtener el ID del usuario
        String query = "SELECT _id FROM usuarios WHERE nombre_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") long userId = cursor.getLong(cursor.getColumnIndex("_id"));
            cursor.close();

            ContentValues contentValues = new ContentValues();
            contentValues.put("password", nuevaContraseña);

            // Actualizar la contraseña en la base de datos
            int rowsAffected = db.update("usuarios", contentValues, "_id = ?", new String[]{String.valueOf(userId)});
            db.close();
            return rowsAffected > 0;
        } else {
            cursor.close();
            db.close();
            return false; // Si el usuario no existe
        }
    }


    public int getRecord(String nombreUsuario, String juego) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Obtener el ID del usuario
        String queryUsuario = "SELECT _id FROM usuarios WHERE nombre_usuario = ?";
        Cursor cursorUsuario = db.rawQuery(queryUsuario, new String[]{nombreUsuario});

        if (!cursorUsuario.moveToFirst()) {
            cursorUsuario.close();
            db.close();
            return -1;  // El usuario no existe
        }

        @SuppressLint("Range") int idUsuario = cursorUsuario.getInt(cursorUsuario.getColumnIndex("_id"));
        cursorUsuario.close();

        // Obtener el ID del juego
        String queryJuego = "SELECT _id FROM juegos WHERE nombre_juego = ?";
        Cursor cursorJuego = db.rawQuery(queryJuego, new String[]{juego});

        if (!cursorJuego.moveToFirst()) {
            cursorJuego.close();
            db.close();
            return -1;  // El juego no existe
        }

        @SuppressLint("Range") int idJuego = cursorJuego.getInt(cursorJuego.getColumnIndex("_id"));
        cursorJuego.close();

        // Consultar el récord del usuario en ese juego
        String queryRecord = "SELECT record FROM records WHERE id_usuario = ? AND id_juego = ?";
        Cursor cursorRecord = db.rawQuery(queryRecord, new String[]{String.valueOf(idUsuario), String.valueOf(idJuego)});

        int record = 0;  // Si no se encuentra el récord, devolver -1

        if (cursorRecord.moveToFirst()) {
            @SuppressLint("Range") int recordActual = cursorRecord.getInt(cursorRecord.getColumnIndex("record"));
            record = recordActual;  // Obtener el récord
        }

        cursorRecord.close();
        db.close(); // Cerrar la base de datos

        return record;
    }

}
