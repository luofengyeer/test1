package com.cmcc.internalcontact.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtils {
    private final String PREFERE_NAME = "PREFERENCES_CONTACT";
    private SharedPreferences preferences;

    public SharePreferencesUtils(Context context) {
        preferences = context.getSharedPreferences(PREFERE_NAME, Context.MODE_PRIVATE);
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
