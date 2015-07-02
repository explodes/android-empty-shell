package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

public class LongPreference extends BasePreference<Long> {

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public LongPreference(SharedPreferences preferences, String name, Long defaultValue) {
        super(preferences, name, defaultValue);
    }

    @Override
    protected Long acquire(SharedPreferences prefs, String name, Long defaultValue) {
        return prefs.getLong(name, defaultValue);
    }

    @Override
    protected void update(SharedPreferences.Editor editor, String name, Long value) {
        editor.putLong(name, value);
    }
}
