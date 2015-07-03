package io.explod.android.emptyshell.meta;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import io.explod.android.emptyshell.module.modules.AppModule;
import io.explod.android.emptyshell.module.modules.PreferenceModule;
import io.explod.android.emptyshell.network.ApiEndpoint;


public class AppInstrumentationTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public AppInstrumentationTest(Class<T> clazz) {
        super(clazz);
    }

    protected void setUp() throws Exception {
        super.setUp();
        clearSharedPreferences(getInstrumentation().getTargetContext());
        enableMockMode(getInstrumentation().getTargetContext());
    }

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
