package io.explod.android.emptyshell.meta;


import dagger.Module;
import io.explod.android.emptyshell.AppTest;

@Module(
        library = false,
        complete = false,
        injects = {
                AppTest.class
        }
)
public class TestModule {

}
