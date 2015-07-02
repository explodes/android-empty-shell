package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

public class BooleanPreference extends BasePreference<Boolean> {

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public BooleanPreference(SharedPreferences preferences, String name, Boolean defaultValue) {
        super(preferences, name, defaultValue);
    }

    @Override
    protected Boolean acquire(SharedPreferences prefs, String name, Boolean defaultValue) {
        return prefs.getBoolean(name, defaultValue);
    }

    @Override
    protected void update(SharedPreferences.Editor editor, String name, Boolean value) {
        editor.putBoolean(name, value);
    }
}
