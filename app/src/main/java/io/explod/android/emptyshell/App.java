package io.explod.android.emptyshell;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import net.danlew.android.joda.JodaTimeAndroid;

import io.explod.android.emptyshell.module.DaggerObjectGraph;
import io.explod.android.emptyshell.module.ObjectGraph;
import io.explod.android.emptyshell.module.modules.AppModule;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

	private ObjectGraph mObjectGraph = DaggerObjectGraph.builder()
			.appModule(new AppModule(this))
			.build();

	public static App getApp(Context context) {
		return ((App) context.getApplicationContext());
	}

	public ObjectGraph getObjectGraph() {
		return mObjectGraph;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (!BuildConfig.DEBUG) {
			// Production Stuff
			onCreateInProductionMode();
		}
		JodaTimeAndroid.init(this);

		//PreferenceModule.clear(this);
	}

	/**
	 * Do things when not in DEBUG mode, onCreate.
	 */
	private void onCreateInProductionMode() {
		Fabric.with(getApplicationContext(), new Crashlytics());
	}
}
