package io.explod.android.emptyshell.module.modules;

import android.app.Application;
import android.util.Log;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.BuildConfig;

@Module
public class AppModule {

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
		Picasso picasso = new Picasso.Builder(context)
			.listener((picasso1, uri, exception) -> Log.d(PICASSO_LOG_TAG, (exception == null ? "Unknown Error" : exception.getMessage()) + ": " + (uri == null ? "no-uri-specified" : uri.toString())))
			.build();
		if (BuildConfig.DEBUG) {
			picasso.setIndicatorsEnabled(true);
		}
		return picasso;
	}
}
