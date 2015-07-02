package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

public class IntegerPreference extends BasePreference<Integer> {

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public IntegerPreference(SharedPreferences preferences, String name, Integer defaultValue) {
        super(preferences, name, defaultValue);
    }

    @Override
    protected Integer acquire(SharedPreferences prefs, String name, Integer defaultValue) {
        return prefs.getInt(name, defaultValue);
    }

    @Override
    protected void update(SharedPreferences.Editor editor, String name, Integer value) {
        editor.putInt(name, value);
    }
}
