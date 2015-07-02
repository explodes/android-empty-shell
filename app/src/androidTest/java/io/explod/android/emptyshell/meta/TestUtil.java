package io.explod.android.emptyshell.meta;

import android.content.Context;
import android.content.SharedPreferences;

import io.explod.android.emptyshell.module.AppModule;
import io.explod.android.emptyshell.module.components.PreferenceModule;
import io.explod.android.emptyshell.network.ApiEndpoint;

public class TestUtil {

    public static void clearSharedPreferences(Context context) {
        PreferenceModule.clear(context);
    }

    public static void enableMockMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AppModule.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PreferenceModule.PREF_API_ENDPOINT, ApiEndpoint.MOCK_MODE.name());
        editor.commit();
    }
}
