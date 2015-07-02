package io.explod.android.emptyshell;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import dagger.ObjectGraph;


public class AppInstrumentationTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public AppInstrumentationTest(Class<T> clazz) {
        super(clazz);
    }

    protected void setUp() throws Exception {
        super.setUp();
        TestUtil.clearSharedPreferences(getInstrumentation().getTargetContext());
        TestUtil.enableMockMode(getInstrumentation().getTargetContext());
        getObjectGraph().inject(this);
    }

    /**
     * Get the ObjectGraph used for testing. Generally all  you'll need to do is add your Test Class to TestModule's {@code injects}
     * parameter.
     */
    protected ObjectGraph getObjectGraph() {
        return App.get(getInstrumentation().getTargetContext()).getObjectGraph().plus(new TestModule());
    }

}
