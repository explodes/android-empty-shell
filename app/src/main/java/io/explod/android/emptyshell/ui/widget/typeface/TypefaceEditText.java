package io.explod.android.emptyshell.ui.widget.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;

import javax.inject.Inject;

import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

import static io.explod.android.emptyshell.util.DependencyInjectionUtils.injectDependencies;


/**
 * TextView that allows a custom font to be set with the custom typeface attribute
 */
public class TypefaceEditText extends EditText {

    @Inject
    TypefaceManager mTypefaceManager;

    private ArrayList<TextWatcher> mListeners = new ArrayList<>();

    public TypefaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);


        // Edit mode does not support custom fonts
        if (!isInEditMode()) {
            injectDependencies(this);
            int[] attrsArray = new int[]{android.R.attr.textStyle};
            TypedArray androidAttrs = context.obtainStyledAttributes(attrs, attrsArray, android.R.attr.editTextStyle, 0);
            int textStyle = androidAttrs.getInt(0, Typeface.NORMAL);
            androidAttrs.recycle();

            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceEditText, android.R.attr.editTextStyle, 0);
            int typefaceEnum = styledAttrs.getInt(R.styleable.TypefaceEditText_typeface, TypefaceManager.FOO_FONT.attributeValue);
            styledAttrs.recycle();

            mTypefaceManager.applyTypeface(this, typefaceEnum, textStyle);
        }
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        mListeners.add(watcher);
    }

    public synchronized void removeAllTextChangedListeners() {
        for (TextWatcher watcher : mListeners)
            removeTextChangedListener(watcher);

        mListeners.clear();
    }


}
