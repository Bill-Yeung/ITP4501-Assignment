package com.example.itp4501_ea_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "GamesLog.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column constants
    public static final String TABLE_GAMES_LOG = "games_log";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAME_DATE = "gameDate";
    public static final String COLUMN_GAME_TIME = "gameTime";
    public static final String COLUMN_OPPONENT_NAME = "opponentName";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_ROUNDS = "rounds";

    // SQL statement to create the games_log table
    private static final String CREATE_TABLE_GAMES_LOG = "CREATE TABLE " + TABLE_GAMES_LOG + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_GAME_DATE + " TEXT NOT NULL, " +
            COLUMN_GAME_TIME + " TEXT NOT NULL, " +
            COLUMN_OPPONENT_NAME + " TEXT, " +
            COLUMN_RESULT + " TEXT NOT NULL, " +
            COLUMN_ROUNDS + " INTEGER NOT NULL);";

    // Constructor for DatabaseHelper
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GAMES_LOG);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

   // add game record
    public boolean addGameLog(String gameDate, String gameTime, String opponentName, String result, int rounds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_DATE, gameDate);
        values.put(COLUMN_GAME_TIME, gameTime);
        values.put(COLUMN_OPPONENT_NAME, opponentName);
        values.put(COLUMN_RESULT, result);
        values.put(COLUMN_ROUNDS, rounds);
        long insertResult = db.insert(TABLE_GAMES_LOG, null, values);
        return insertResult != -1;
    }

    // show the game logs in descending order
    public Cursor getAllGameLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_GAMES_LOG, null, null, null, null, null, COLUMN_ID + " DESC");
    }
}