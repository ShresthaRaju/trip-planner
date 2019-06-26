package com.raju.tripplanner.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.raju.tripplanner.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AUTH_USER";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_AUTH_USER_TABLE = "CREATE TABLE IF NOT EXISTS auth_user (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, familyName TEXT," +
            " email TEXT, username TEXT, display_picture TEXT ) ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AUTH_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertAuthUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", user.getFirstName());
        contentValues.put("familyName", user.getFamilyName());
        contentValues.put("email", user.getEmail());
        contentValues.put("username", user.getUsername());
        contentValues.put("display_picture", user.getDisplayPicture());

        getWritableDatabase().insert("auth_user", " ", contentValues);
    }
}
