package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;

import java.util.Set;

public class StringSetPreference extends BasePreference<Set<String>> {

	/**
	 * Construct this setting
	 *
	 * @param preferences  The SharedPreferences to get/set from/to
	 * @param name         Name of the setting
	 * @param defaultValue Default value of the setting
	 */
	public StringSetPreference(SharedPreferences preferences, String name, Set<String> defaultValue) {
		super(preferences, name, defaultValue);
	}

	@Override
	protected Set<String> acquire(SharedPreferences prefs, String name, Set<String> defaultValue) {
		return prefs.getStringSet(name, defaultValue);
	}

	@Override
	protected void update(SharedPreferences.Editor editor, String name, Set<String> value) {
		editor.putStringSet(name, value);
	}
}
