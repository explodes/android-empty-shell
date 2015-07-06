package io.explod.android.emptyshell.module.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.Constants;
import io.explod.android.emptyshell.module.annotations.NamedPreference;
import io.explod.android.emptyshell.network.ApiEndpoint;
import io.explod.android.emptyshell.util.prefs.EnumPreference;

@Module
public class PreferenceModule {

	public static final String PREF_API_ENDPOINT = "api_endpoint";

	public static void clear(Context context) {
		context.getApplicationContext()
				.getSharedPreferences(AppModule.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
				.edit()
				.clear()
				.commit();
	}

	@Provides
	@Singleton
	@NamedPreference(PREF_API_ENDPOINT)
	EnumPreference<ApiEndpoint> providesTargetApiEndpoint(SharedPreferences preferences) {
		return new EnumPreference<>(preferences, PREF_API_ENDPOINT, Constants.DEFAULT_ENDPOINT);
	}

}
