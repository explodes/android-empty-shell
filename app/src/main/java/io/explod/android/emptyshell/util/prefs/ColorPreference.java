package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;
import android.graphics.Color;

public class ColorPreference extends BasePreference<Integer> {

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public ColorPreference(SharedPreferences preferences, String name, Integer defaultValue) {
        super(preferences, name, defaultValue);
    }

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public ColorPreference(SharedPreferences preferences, String name, String defaultValue) {
        this(preferences, name, parseColor(defaultValue, null));
    }

    @Override
    protected Integer acquire(SharedPreferences prefs, String name, Integer defaultValue) {
        return prefs.getInt(name, defaultValue);
    }

    @Override
    protected void update(SharedPreferences.Editor editor, String name, Integer value) {
        editor.putInt(name, value);
    }

    public void set(String value) {
        Integer color = null;
        if (value != null) {
            color = parseColor(value, getDefaultValue());
        }
        super.set(color);
    }

    private static Integer parseColor(String color, Integer defaultValue) {
        try {
            return Color.parseColor(color);
        } catch (IllegalArgumentException ex) {
            // bad color
            return defaultValue;
        }
    }
}