package com.developer.manuelquinteros.crud_prueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final  String DATABASE_NAME = "crud_db";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
         //Create crud table
        db.execSQL(UserData.CREATE_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserData.TABLE_CRUD);

        //Create tables again
        onCreate(db);
    }

    public long insertCrud(String name, String email) {
        //get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserData.COLUMN_NAME, name);
        values.put(UserData.COLUMN_EMAIL, email);

        //insert row
        long id = db.insert(UserData.TABLE_CRUD, null, values);

        //close db connection
        db.close();

        //return newly inserted row id
        return id;
    }

    public UserData getCrud(long id) {
        //get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserData.TABLE_CRUD,
                new String[]{UserData.COLUMN_ID, UserData.COLUMN_NAME, UserData.COLUMN_EMAIL}, UserData.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //prepare crud object
        UserData userData = new UserData(
                cursor.getInt(cursor.getColumnIndex(UserData.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(UserData.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(UserData.COLUMN_EMAIL)));
        //close the db connection
        cursor.close();

        return userData;

    }

    public List<UserData> getAllCrud() {
        List<UserData> crud = new ArrayList<>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + UserData.TABLE_CRUD + " ORDER BY " + UserData.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserData userData = new UserData();
                userData.setId(cursor.getInt(cursor.getColumnIndex(UserData.COLUMN_ID)));
                userData.setName(cursor.getString(cursor.getColumnIndex(UserData.COLUMN_NAME)));
                userData.setEmail(cursor.getString(cursor.getColumnIndex(UserData.COLUMN_EMAIL)));

                crud.add(userData);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return crud list
        return crud;
    }

    public int getCrudCount() {
        String countQuery = "SELECT * FROM " + UserData.TABLE_CRUD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        //return count
        return count;
    }

    public int updateCrud(UserData userData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserData.COLUMN_NAME, userData.getName());
        values.put(UserData.COLUMN_EMAIL, userData.getEmail());

        //Updating row
        return db.update(UserData.TABLE_CRUD, values, UserData.COLUMN_ID + " = ?", new String[]{String.valueOf(userData.getId())});
    }

    public void deleteCrud(UserData userData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserData.TABLE_CRUD, UserData.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userData.getId())});
        db.close();
    }


    public boolean validarRepeticionCrud(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean operacion = false;

        try
        {
            final String query = "SELECT * FROM " + UserData.TABLE_CRUD + " WHERE " + UserData.COLUMN_NAME + " = '" + name + "'";
            Cursor fila = db.rawQuery(query, null);

            while (fila.moveToNext())
            {
                String nom = fila.getString(1);
                if (name.equals(nom)) {
                    operacion = true;
                }
            }

        }catch (SQLiteException e) {
            new SQLiteException("Error al Insertar");
        }
        catch (Exception e)
        {
            new Exception("Error en el metodo Insertar");
        }
        return operacion;
    }
}
