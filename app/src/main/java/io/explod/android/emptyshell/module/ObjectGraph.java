package io.explod.android.emptyshell.module;

import javax.inject.Singleton;

import dagger.Component;
import io.explod.android.emptyshell.module.modules.AppModule;
import io.explod.android.emptyshell.module.modules.NetworkModule;
import io.explod.android.emptyshell.module.modules.PreferenceModule;
import io.explod.android.emptyshell.module.modules.TypefaceModule;

@Component(
    modules = {
        AppModule.class,
        NetworkModule.class,
        PreferenceModule.class,
        TypefaceModule.class
    }
)
@Singleton
public interface ObjectGraph extends Injections{
}
