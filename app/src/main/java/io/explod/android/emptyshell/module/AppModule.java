package io.explod.android.emptyshell.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import io.explod.android.emptyshell.App;
import io.explod.android.emptyshell.module.annotations.ForApplication;
import io.explod.android.emptyshell.module.components.NetworkModule;
import io.explod.android.emptyshell.module.components.PreferenceModule;
import io.explod.android.emptyshell.module.components.TypefaceModule;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = true,
        library = true,
        includes = {
                // List Components Here
                PreferenceModule.class,
                NetworkModule.class,
                TypefaceModule.class
        },
        injects = {
                // List injected classes here
                // ## App
                App.class,
        }
)
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
        return new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d(PICASSO_LOG_TAG, (exception == null ? "Unknown Error" : exception.getMessage()) + ": " + (uri == null ? "no-uri-specified" : uri.toString()));
            }
        }).build();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(@ForApplication Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

}
