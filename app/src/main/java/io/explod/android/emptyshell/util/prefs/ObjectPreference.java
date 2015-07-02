package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class ObjectPreference<T> extends StringPreference {

    private Type mType;
    private Gson mGson;

    /**
     * Construct this setting
     *
     * @param gson         Gson JSON converter
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public ObjectPreference(Type type, Gson gson, SharedPreferences preferences, String name, T defaultValue) {
        super(preferences, name, encode(gson, defaultValue));
        mType = type;
        mGson = gson;
    }

    public synchronized T getObject() {
        return decode(mGson, mType, get());
    }

    public synchronized void setObject(T object) {
        String converted = encode(mGson, object);
        set(converted);
    }

    private static String encode(Gson gson, Object value) {
        if (value == null) {
            return null;
        } else {
            return gson.toJson(value);
        }
    }

    private static <T> T decode(Gson gson, Type type, String value) {
        if (value == null) {
            return null;
        } else {
            return gson.fromJson(value, type);
        }
    }
}
