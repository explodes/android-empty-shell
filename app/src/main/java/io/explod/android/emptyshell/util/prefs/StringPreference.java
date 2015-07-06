package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

public class StringPreference extends BasePreference<String> {

	/**
	 * Construct this setting
	 *
	 * @param preferences  The SharedPreferences to get/set from/to
	 * @param name         Name of the setting
	 * @param defaultValue Default value of the setting
	 */
	public StringPreference(SharedPreferences preferences, String name, String defaultValue) {
		super(preferences, name, defaultValue);
	}

	@Override
	protected String acquire(SharedPreferences prefs, String name, String defaultValue) {
		return prefs.getString(name, defaultValue);
	}

	@Override
	protected void update(SharedPreferences.Editor editor, String name, String value) {
		editor.putString(name, value);
	}
}
