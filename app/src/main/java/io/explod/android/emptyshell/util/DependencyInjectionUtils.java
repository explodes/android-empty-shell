package io.explod.android.emptyshell.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import io.explod.android.emptyshell.App;


public class DependencyInjectionUtils {

    public static <T> void injectDependencies(Context context, T obj) {
        App.get(context).getObjectGraph().inject(obj);
    }

    public static void injectDependencies(Context context) {
        injectDependencies(context, context);
    }

    public static void injectDependencies(Fragment fragment) {
        injectDependencies(fragment.getActivity(), fragment);
    }

    public static void injectDependencies(View view) {
        injectDependencies(view.getContext(), view);
    }
}
