package io.explod.android.emptyshell.module.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.BuildConfig;

@Module
public class AppModule {

	public static final String SHARED_PREFS_NAME = "prefs";

	private static final String PICASSO_LOG_TAG = "Picasso";

	private Application mApplication;

	public AppModule(Application application) {
		mApplication = application;
	}

	@Provides
	@Singleton
	Application providesApplication() {
		return mApplication;
	}

	@Provides
	@Singleton
	Picasso providesPicasso(Application context) {
		Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
			@Override
			public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
				String message = exception == null ? "Unknown Error" : exception.getMessage();
				String uriMessage = uri == null ? "no-uri-specified" : uri.toString();
				Log.d(PICASSO_LOG_TAG, message + ": " + uriMessage);
			}
		}).build();
		if (BuildConfig.DEBUG) {
			picasso.setIndicatorsEnabled(true);
		}
		return picasso;
	}

	@Provides
	@Singleton
	SharedPreferences providesSharedPreferences(Application context) {
		return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	}
}
