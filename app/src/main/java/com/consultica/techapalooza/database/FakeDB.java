package com.consultica.techapalooza.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dimadruchinin on 28.11.15.
 */
public class FakeDB {

    private static final String NAME = "local_techapalooza_settings";

    public static final String EMAIL = "local_techapalooza_email";
    private static final String KEY_PASSWORD = "local_techapalooza_password";
    private static final String USER_ID = "local_techapalooza_user_id";
    private static final String CAN_REDEEM = "local_techapalooza_can_redeem";


    private Context context;
    private SharedPreferences pref;

    private static FakeDB instance;

    public static FakeDB getInstance(Context context) {
        if (instance == null) {
            instance = new FakeDB(context);
        }

        return instance;
    }

    private FakeDB(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
    }

    public FakeDB saveString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
        return this;
    }

    public FakeDB saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }

    public FakeDB delete(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
        return this;
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveEmail(String email){
        saveString(EMAIL, email);
    }

    public String getEmail(){
        return getString(EMAIL);
    }

    public void savePassword(String password) {
        saveString(KEY_PASSWORD, password);
    }

    public void resetLoginAndPassword() {
        saveString(EMAIL, "");
        saveString(KEY_PASSWORD, "");
        saveString(USER_ID, "");
    }

    public String getPassword() {
        return getString(KEY_PASSWORD);
    }


    public void saveUserId(String userId){
        saveString(USER_ID, userId);
    }

    public String getUserId(){
        return getString(USER_ID);
    }

    public void saveCanRedeem(boolean canRedeem) {
        saveBoolean(CAN_REDEEM, canRedeem);
    }

    public boolean getCanRedeem(){
        return getBoolean(CAN_REDEEM);
    }
}
