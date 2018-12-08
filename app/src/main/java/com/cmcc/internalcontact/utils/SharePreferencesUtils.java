package com.cmcc.internalcontact.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtils {
    private final String PREFERE_NAME = "PREFERENCES_CONTACT";
    private static SharedPreferences preferences;
    private static SharePreferencesUtils instance;
    private static Context context;

    public static SharePreferencesUtils getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new SharePreferencesUtils();
        return instance;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
        preferences = context.getSharedPreferences(PREFERE_NAME, Context.MODE_PRIVATE);
    }

    private SharePreferencesUtils() {
    }

    public void setString(String key, String value) {
        if (preferences == null) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String getString(String key, String defaultValue) {
        if (preferences == null) {
            return null;
        }
        return preferences.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {
        if (preferences == null) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public void setLong(String key, long value) {
        if (preferences == null) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public long getLong(String key, long value) {
        if (preferences == null) {
            return value;
        }
        return preferences.getLong(key, value);
    }

    public void setBoolean(String key, boolean value) {
        if (preferences == null) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public boolean getBoolean(String key) {
        if (preferences == null) {
            return false;
        }
        return preferences.getBoolean(key, false);
    }

    public int setInt(String key) {
        if (preferences == null) {
            return -1;
        }
        return preferences.getInt(key, -1);
    }
}
