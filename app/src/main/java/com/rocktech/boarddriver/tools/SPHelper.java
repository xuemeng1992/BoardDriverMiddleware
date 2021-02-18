package com.rocktech.boarddriver.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {

    private static SPHelper sharedPreferencesHelper = null;

    //单例模式，把Context传进去
    public static SPHelper getInstance(Context context) {

        if (sharedPreferencesHelper == null) {
            synchronized (SPHelper.class) {
                if (sharedPreferencesHelper == null) {
                    sharedPreferencesHelper = new SPHelper();
                    sharedPreferencesHelper.setContext(context);
                    return sharedPreferencesHelper;
                }
            }
        }

        return sharedPreferencesHelper;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    //取Boolean 型数据
    public boolean getBoolean(String key, boolean defValue) {
        try {
            return getSP().getBoolean(key, defValue);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return defValue;
        }
    }

    //存Boolean 型数据
    public void putBoolean(String key, boolean value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    //取Long 型数据
    public long getLong(String key, long defValue) {
        try {
            return getSP().getLong(key, defValue);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return defValue;
        }
    }

    //存Long 型数据
    public void putLong(String key, long value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putLong(key, value);
            editor.commit();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    //取整型
    public int getInt(String key, int defaultValue) {
        try {
            return getSP().getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    //存整型
    public void putInt(String key, int value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取String
    public String getString(String key, String defValue) {
        try {
            return getSP().getString(key, defValue);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    //存String
    public void putString(String key, String value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putString(key, value);
            editor.commit();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //清除数据
    public void clear() {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.clear();
            editor.commit();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //获得SharedPreferences对象
    private SharedPreferences getSP() {
        return context.getSharedPreferences("rocktech", Context.MODE_PRIVATE);
    }

    private SharedPreferences getSP(String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


}
