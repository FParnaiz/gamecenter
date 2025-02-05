package com.example.gamecenter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.function.BinaryOperator;

public class DBhelper extends  SQLiteOpenHelper{

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
        // First, check if the user already exists
        if (!checkUsuarioExist(nombre)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("nombre_usuario", nombre);
            contentValues.put("password", password);

            // Insert the user and return the row ID of the newly inserted record
            return db.insert("usuarios", null, contentValues);
        } else {
            // User already exists, return -1
            return -1;
        }
    }

    public boolean checkUsuarioExist(String nombre){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombre});


        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    public boolean checkPassword(String nombre, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombre});

        if(cursor.moveToFirst()){
            @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password"));

            // Close the cursor after usage
            cursor.close();

            // Check if the stored password matches the provided password
            return storedPassword != null && storedPassword.equals(password);
        }else{
            cursor.close();
            return false;
        }
    }

    public ArrayList<InfoJuego> getJuegos(String usuario){
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if usuario is null
        if (usuario == null) {
            Log.e("DBhelper", "getJuegos: usuario is null!");
            return new ArrayList<>(); // Return an empty list instead of crashing
        }

        String query = "SELECT juegos.nombre_juego, records.record " +
                "FROM records " +
                "JOIN juegos ON records.id_juego = juegos._id " +
                "JOIN usuarios ON records.id_usuario = usuarios._id " +
                "WHERE usuarios.nombre_usuario = ?";

        Cursor cursor = db.rawQuery(query, new String[]{usuario});

        ArrayList<InfoJuego> juegos = new ArrayList<>();

        // Ensure cursor has data before accessing it
        if (cursor.moveToFirst()) {  // Check if cursor has at least one row
            do {
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("nombre_juego"));
                @SuppressLint("Range") int record = cursor.getInt(cursor.getColumnIndex("record"));
                juegos.add(new InfoJuego(titulo, record, usuario));
            } while (cursor.moveToNext());
        }else {
            query="SELECT nombre_juego FROM juegos";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("nombre_juego"));
                    juegos.add(new InfoJuego(titulo, 0, usuario));
                } while (cursor.moveToNext());

            }
        }

        cursor.close();
        return juegos;
    }

    public Boolean borrarRecords(String usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM records WHERE id_usuario = (SELECT _id FROM usuarios WHERE nombre_usuario = ?)";
        int rowsAffected = db.delete("records", query, new String[]{usuario});
        return rowsAffected > 0;
    }






}



