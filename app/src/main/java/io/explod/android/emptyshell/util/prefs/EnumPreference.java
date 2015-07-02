package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

public class EnumPreference<T extends Enum<T>> extends BasePreference<T> {

    /**
     * Construct this setting
     *
     * @param preferences  The SharedPreferences to get/set from/to
     * @param name         Name of the setting
     * @param defaultValue Default value of the setting
     */
    public EnumPreference(SharedPreferences preferences, String name, T defaultValue) {
        super(preferences, name, defaultValue);
    }

    @Override
    protected T acquire(SharedPreferences prefs, String name, T defaultValue) {
        String value = prefs.getString(name, defaultValue.name());
        for (T t : defaultValue.getDeclaringClass().getEnumConstants()) {
            if (t.name().equals(value)) {
                return t;
            }
        }
        return defaultValue;
    }

    @Override
    protected void update(SharedPreferences.Editor editor, String name, T value) {
        editor.putString(name, value.name());
    }
}
