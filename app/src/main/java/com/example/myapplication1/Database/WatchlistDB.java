package com.example.myapplication1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication1.Modals.Movie;
import com.example.myapplication1.Modals.OTT;
import com.example.myapplication1.Parameters.OTTParams;
import com.example.myapplication1.Parameters.watchParams;

import java.util.ArrayList;

public class Watchlist extends SQLiteOpenHelper {

    public Watchlist(Context context){
        super(context, new watchParams().uid, null, new watchParams().Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + new watchParams().TBName
                + "(ID TEXT PRIMARY KEY, Title TEXT, Poster TEXT)";
        db.execSQL(create);
    }

    public boolean ifExists(String ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String checkQuery = "SELECT " + new watchParams().ID + " FROM " + new watchParams().TBName
                + " WHERE " + new watchParams().ID + "= '"+ID + "'";
        cursor= db.rawQuery(checkQuery,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }



    /*public String getUserName(String platform){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String checkQuery = "SELECT " + new OTTParams().KEY_UserName + " FROM " + new OTTParams().TBName
                + " WHERE " + new OTTParams().KEY_PLATFORM + "= '"+platform + "'";
        cursor = db.rawQuery(checkQuery, null);
        String userName = "";
        if(cursor.moveToFirst()) {
            //userName = cursor.getString(cursor.getColumnIndex(" " + new OTTParams().KEY_UserName));
            userName = cursor.getString(0) == null ? "" : cursor.getString(0);
        }
        cursor.close();
        return userName;
    }*/

    public void addMovie(Movie movie){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(new watchParams().ID, movie.getId() == null ? "" : movie.getId());
        contentValues.put(new watchParams().title, movie.getTitle()== null ? "" : movie.getTitle());
        contentValues.put(new watchParams().url, movie.getUrl()== null ? "" : movie.getUrl());
        sqLiteDatabase.insert(new watchParams().TBName, null, contentValues);
        sqLiteDatabase.close();
    }

    public void update(Movie movie){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(new watchParams().ID, movie.getId() == null ? "" : movie.getId());
        contentValues.put(new watchParams().title, movie.getTitle()== null ? "" : movie.getTitle());
        contentValues.put(new watchParams().url, movie.getUrl()== null ? "" : movie.getUrl());
        if(ifExists(movie.getId())){
            sqLiteDatabase.update(new watchParams().TBName, contentValues,
                    new watchParams().ID + "=?", new String[]{movie.getId()});
            sqLiteDatabase.close();
        }
        else{
            addMovie(movie);
        }
    }

    public ArrayList<Movie> getMovieList(){
        ArrayList<Movie> moviesList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String select = "SELECT * FROM " + new watchParams().TBName;
        Cursor cursor = sqLiteDatabase.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                Movie movie = new Movie();
                movie.setId(cursor.getString(0) == null ? "":cursor.getString(0));
                movie.setTitle(cursor.getString(1) == null ? "":cursor.getString(1));
                movie.setUrl(cursor.getString(2) == null ? "":cursor.getString(2));
                moviesList.add(movie);
            }while(cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return moviesList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + new watchParams().TBName);
        onCreate(db);
    }

    /*public void updateName(String platform, String name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "UPDATE " + new OTTParams().TBName +" SET "
                + new OTTParams().KEY_UserName + " = '" + name + "' WHERE " + new OTTParams().KEY_PLATFORM + " = '" + platform + "'";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public void updateDate(String platform, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "UPDATE " + new OTTParams().TBName +" SET "
                + new OTTParams().KEY_Date + " = '" + date + "' WHERE " + new OTTParams().KEY_PLATFORM + " = '" + platform + "'";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }*/

    public void drop(){
        SQLiteDatabase db = this.getWritableDatabase();
        String Drop = "DROP TABLE IF EXISTS " + new watchParams().TBName;
        db.execSQL(Drop);
        onCreate(db);
    }

    public int deleteMovie(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(new watchParams().TBName, new watchParams().ID + "=?", new String[]{id});
    }

}