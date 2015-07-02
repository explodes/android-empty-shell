package io.explod.android.emptyshell.util.prefs;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Iterator;

import io.explod.android.emptyshell.BuildConfig;
import io.explod.android.emptyshell.util.WeakList;

/**
 * @param <T> Type of setting to save.
 */
public abstract class BasePreference<T> {

    // inner

    /**
     * Optional listener for settings changes
     */
    public interface SettingListener<T> {
        /**
         * Fired when settings change.
         *
         * @param preferences The SharedPreferences updated
         * @param name        The name of the preference
         * @param value       The new value
         */
        void settingChanged(SharedPreferences preferences, String name, T value);
    }

    // static

    private static final String TAG = "BasePreference";

    // instance

    /**
     * Preferences store to save to
     */
    private SharedPreferences mPreferences;
    /**
     * Name of the preference
     */
    private String mPrefenceName;
    /**
     * Default setting value
     */
    private T mDefaultValue;
    /**
     * Settings changed listeners
     */
    private WeakList<SettingListener<T>> mSettingListeners = new WeakList<>();


    /**
     * Construct a new setting
     *
     * @param preferences    The SharedPreferences to get/set from/to
     * @param preferenceName The name of the preference
     * @param defaultValue   Default setting value
     */
    public BasePreference(SharedPreferences preferences, String preferenceName, T defaultValue) {
        mPreferences = preferences;
        mPrefenceName = preferenceName;
        mDefaultValue = defaultValue;
    }

    /**
     * @return Returns the name of this setting
     */
    public String getPreferenceName() {
        return mPrefenceName;
    }

    /**
     * @return Returns the default value
     */
    protected T getDefaultValue() {
        return mDefaultValue;
    }

    /**
     * Save the value to preferences
     *
     * @param value Value to save
     */
    public void set(T value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        String name = getPreferenceName();
        update(editor, name, value);
        editor.apply();
        notifyListeners(value);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "set " + name + " to " + value);
        }
    }

    /**
     * Get the setting from preferences
     *
     * @return The setting in preferences or its default value
     */
    public T get() {
        T defaultValue = getDefaultValue();
        String name = getPreferenceName();
        T value = acquire(mPreferences, name, defaultValue);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "get " + name + " as " + value);
        }
        return value;
    }

    private void notifyListeners(T value) {
        Iterator<SettingListener<T>> iterator = mSettingListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().settingChanged(mPreferences, getPreferenceName(), value);
        }
    }

    public void addSettingListener(SettingListener<T> listener) {
        mSettingListeners.add(listener);
    }

    public void removeSettingListener(SettingListener<T> listener) {
        mSettingListeners.remove(listener);
    }

    /**
     * Perform the actual updating. Do not {@link SharedPreferences.Editor#commit()} or {@link SharedPreferences.Editor#apply()}
     *
     * @param editor The {@link SharedPreferences.Editor} to save the value to
     * @param name   Name of the setting to update
     * @param value  The value to save
     */
    protected abstract void update(SharedPreferences.Editor editor, String name, T value);

    /**
     * Actually pull the value from preferences
     *
     * @param prefs        {@link SharedPreferences} to pull from
     * @param name         Name of the setting to acquire
     * @param defaultValue The default value to return
     * @return The value from {@link SharedPreferences} or the default value
     */
    protected abstract T acquire(SharedPreferences prefs, String name, T defaultValue);

}
