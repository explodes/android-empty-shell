package io.explod.android.emptyshell.module.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.BuildConfig;
import io.explod.android.emptyshell.module.annotations.ForApplication;

@Module
public class AppModule {

    public static final String SHARED_PREFS_NAME = "prefs";

    private static final String PICASSO_LOG_TAG = "Picasso";

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    @ForApplication
    Context providesApplicationContext() {
        return mContext;
    }

    @Provides
    @Singleton
    Picasso providesPicasso(@ForApplication Context context) {
        Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d(PICASSO_LOG_TAG, (exception == null ? "Unknown Error" : exception.getMessage()) + ": " + (uri == null ? "no-uri-specified" : uri.toString()));
            }
        }).build();
        if (BuildConfig.DEBUG) {
            picasso.setIndicatorsEnabled(true);
        }
        return picasso;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(@ForApplication Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }
}
