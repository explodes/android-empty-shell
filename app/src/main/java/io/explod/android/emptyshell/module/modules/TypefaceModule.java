package io.explod.android.emptyshell.module.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

@Module
public class TypefaceModule {

    @Provides
    @Singleton
    TypefaceManager providesTypefaceManager() {
        return new TypefaceManager();
    }
}
