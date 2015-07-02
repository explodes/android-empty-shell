package io.explod.android.emptyshell.module.components;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

@Module(
        library = true,
        complete = true
)
public class TypefaceModule {

    @Provides
    @Singleton
    TypefaceManager providesTypefaceManager() {
        return new TypefaceManager();
    }
}
