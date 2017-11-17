package com.zentcode.parks.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private Context mContext;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public PreferenceManager() {}

    public PreferenceManager(Context context) {
        this.mContext = context;
    }

    public void saveString(String key, String value) {
        sp = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        sp = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public void saveInt(String key, Integer value) {
        sp = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
        editor.putInt(key, value);
        editor.apply();
    }

    public Integer getInt(String key) {
        sp = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public void clearPreference(String key) {
        sp = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
