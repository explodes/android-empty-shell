package io.explod.android.emptyshell.module.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.Constants;
import io.explod.android.emptyshell.network.ApiEndpoint;
import io.explod.android.emptyshell.util.prefs.BooleanPreference;
import io.explod.android.emptyshell.util.prefs.EnumPreference;

@Module
public class PreferenceModule {


	public static final String SHARED_PREFS_NAME = "prefs";

	public static final String PREF_MOCK = "is_mock_mode";

	public static final String PREF_API_ENDPOINT = "api_endpoint";

	@Provides
	@Singleton
	SharedPreferences providesSharedPreferences(Application context) {
		return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	}

	public static void clear(Context context) {
		context.getApplicationContext()
			.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
			.edit()
			.clear()
			.commit();
	}

	@Provides
	@Singleton
	@Named(PREF_MOCK)
	BooleanPreference providesIsMockMode(SharedPreferences preferences) {
		return new BooleanPreference(preferences, PREF_MOCK, false);
	}

	@Provides
	@Singleton
	@Named(PREF_API_ENDPOINT)
	EnumPreference<ApiEndpoint> providesTargetApiEndpoint(SharedPreferences preferences) {
		return new EnumPreference<>(preferences, PREF_API_ENDPOINT, Constants.DEFAULT_ENDPOINT);
	}

}
