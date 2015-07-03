package io.explod.android.emptyshell.ui.widget.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import javax.inject.Inject;

import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

import static io.explod.android.emptyshell.App.getApp;


/**
 * TextView that allows a custom font to be set with the custom typeface attribute
 */
public class TypefaceTextView extends TextView {

    @Inject
    TypefaceManager mTypefaceManager;

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Edit mode does not support custom fonts
        if (!isInEditMode()) {
            getApp(context).getObjectGraph().inject(this);
            int[] attrsArray = new int[]{android.R.attr.textStyle};
            TypedArray androidAttrs = context.obtainStyledAttributes(attrs, attrsArray, android.R.attr.textViewStyle, 0);
            int textStyle = androidAttrs.getInt(0, Typeface.NORMAL);
            androidAttrs.recycle();

            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView, android.R.attr.textViewStyle, 0);
            int mTypefaceAttributeValue = styledAttrs.getInt(R.styleable.TypefaceTextView_typeface, TypefaceManager.FOO_FONT.attributeValue);
            styledAttrs.recycle();

            mTypefaceManager.applyTypeface(this, mTypefaceAttributeValue, textStyle);
        }
    }
}
