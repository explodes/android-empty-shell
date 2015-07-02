package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

public class FloatPreference extends BasePreference<Float> {

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public FloatPreference(SharedPreferences preferences, String name, Float defaultValue) {
        super(preferences, name, defaultValue);
    }

    @Override
    protected Float acquire(SharedPreferences prefs, String name, Float defaultValue) {
        return prefs.getFloat(name, defaultValue);
    }

    @Override
    protected void update(SharedPreferences.Editor editor, String name, Float value) {
        editor.putFloat(name, value);
    }
}
