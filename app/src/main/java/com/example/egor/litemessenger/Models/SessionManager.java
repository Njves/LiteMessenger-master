package com.example.egor.litemessenger.Models;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    public static final String TAG = SessionManager.class.getSimpleName();
    SharedPreferences mPreferences;
    Editor mEditor;
    Context _context;
    public static final String PREF_NAME = "LtMessengerLogin";
    public static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        _context = context;
        mEditor = mPreferences.edit();

    }
    public void setLogin(boolean isLoggedIn) {

        mEditor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        mEditor.apply();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return mPreferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }



}
