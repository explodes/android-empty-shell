package io.explod.android.emptyshell;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.danlew.android.joda.JodaTimeAndroid;

import io.explod.android.emptyshell.module.DaggerObjectGraph;
import io.explod.android.emptyshell.module.ObjectGraph;
import io.explod.android.emptyshell.module.modules.AppModule;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

	private static App sInstance;

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

		if (!BuildConfig.DEBUG) {
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

}
