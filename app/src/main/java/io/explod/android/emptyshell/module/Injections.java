package io.explod.android.emptyshell.module;

import io.explod.android.emptyshell.ui.activity.MainActivity;
import io.explod.android.emptyshell.ui.widget.typeface.CustomTypefaceSpan;
import io.explod.android.emptyshell.ui.widget.typeface.TypefaceButton;
import io.explod.android.emptyshell.ui.widget.typeface.TypefaceEditText;
import io.explod.android.emptyshell.ui.widget.typeface.TypefaceTextView;

public interface Injections {

    void inject(TypefaceButton target);

    void inject(TypefaceTextView target);

    void inject(CustomTypefaceSpan target);

    void inject(TypefaceEditText target);

    void inject(MainActivity target);

}
