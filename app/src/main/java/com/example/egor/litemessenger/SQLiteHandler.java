package com.example.egor.litemessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {
    public static final String TAG = SQLiteHandler.class.getSimpleName();
    public static final String NAME = "LiteMessengerDatabase";
    public static final int VERSION = 1;
    public static final String TABLE_USER = "users";
    public static final String TABLE_ID = "_id";
    public static final String USER_LOGIN = "login";
    public static final String USER_EMAIL = "email";
    public static final String USER_UID = "uid";
    public static final String USER_CREATED_AT = "created_at";
    public SQLiteHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + TABLE_ID + " INTEGER PRIMARY KEY," + USER_LOGIN + " TEXT,"
                + USER_EMAIL + " TEXT UNIQUE," + USER_UID + " TEXT,"
                + USER_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addUser(String login,String email, String uid, String date)
    {
        SQLiteDatabase sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_LOGIN,login);
        values.put(USER_EMAIL,email);
        values.put(USER_CREATED_AT,date);
        values.put(USER_UID,uid);
        long id = sqlite.insert(TABLE_USER, null, values);
        sqlite.close();
        Log.d(TAG, "id" + id);

    }
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] countResult = cursor.getColumnNames();

        Log.i(TAG, Arrays.toString(countResult) + cursor.getColumnCount() + " count " + cursor.getCount());
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("_id", cursor.getString(0));
            user.put("login", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            int login = cursor.getColumnIndex(USER_LOGIN);
            int email = cursor.getColumnIndex(USER_EMAIL);
            int uid = cursor.getColumnIndex(USER_UID);
            int created_at = cursor.getColumnIndex(USER_CREATED_AT);
            Log.d(TAG,cursor.getString(login) + " " +
                    cursor.getString(email) + " " +
                    cursor.getString(uid)  + " " +
                    cursor.getString(created_at));

        }


        cursor.close();
        db.close();
        return user;
    }
    public void deleteUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
    }
}
