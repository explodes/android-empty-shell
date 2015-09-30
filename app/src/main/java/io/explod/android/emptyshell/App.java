package io.explod.android.emptyshell;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.danlew.android.joda.JodaTimeAndroid;

import io.explod.android.emptyshell.module.DaggerObjectGraph;
import io.explod.android.emptyshell.module.ObjectGraph;
import io.explod.android.emptyshell.module.modules.AppModule;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

	// statics

	public static void logException(Throwable error) {
		logException(TAG, "An exception has occurred", error);
	}

	public static void logException(String tag, String message, Throwable error) {
		Log.e(tag, message, error);
		if (!BuildConfig.DEBUG) {
			Crashlytics.logException(error);
		}
	}

	public static void showException(@Nullable Context context, String tag, String errorMessage, @StringRes int userMessage, Throwable error) {
		logException(tag, errorMessage, error);
		if (context != null) {
			sMainHandler.post(() -> Toast.makeText(context, userMessage, Toast.LENGTH_SHORT).show());
		}
	}

	private static final String TAG = App.class.getSimpleName();

	private static App sInstance;

	private static Handler sMainHandler = new Handler(Looper.getMainLooper());

	// instance

	private ObjectGraph mObjectGraph = DaggerObjectGraph.builder()
		.appModule(new AppModule(this))
		.build();

	public static App getApp() {
		return sInstance;
	}

	public ObjectGraph getObjectGraph() {
		return mObjectGraph;
	}

	public static RefWatcher getRefWatcher() {
		return getApp().refWatcher;
	}

	private RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();

		sInstance = this;

		if (BuildConfig.DEBUG) {
			// Debug Stuff
			onCreateDebugMode();
		}

		if ("release".equals(BuildConfig.BUILD_NAME) ||  "staging".equals(BuildConfig.BUILD_NAME)) {
			// Production Stuff
			onCreateInProductionMode();
		}

		JodaTimeAndroid.init(this);

		refWatcher = LeakCanary.install(this);

		//PreferenceModule.clear(this);
	}

	/**
	 * Do things when not in DEBUG mode, onCreate.
	 */
	private void onCreateInProductionMode() {
		Fabric.with(getApplicationContext(), new Crashlytics());
	}

	private void onCreateDebugMode() {
		enabledStrictMode();
	}

	private void enabledStrictMode() {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//				.detectAll()
//				.penaltyLog()
//				.penaltyDeath()
//				.build());
	}

}
