package io.explod.android.emptyshell.ui.widget.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import javax.inject.Inject;

import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

import static io.explod.android.emptyshell.App.getApp;


/**
 * TextView that allows a custom font to be set with the custom typeface attribute
 */
public class TypefaceButton extends Button {

    @Inject
    TypefaceManager mTypefaceManager;

    public TypefaceButton(Context context) {
        this(context, null);
    }

    public TypefaceButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public TypefaceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Edit mode does not support custom fonts
        if (!isInEditMode()) {
            getApp(context).getObjectGraph().inject(this);
            int[] attrsArray = new int[]{android.R.attr.textStyle};
            TypedArray androidAttrs = context.obtainStyledAttributes(attrs, attrsArray, android.R.attr.buttonStyle, 0);
            int textStyle = androidAttrs.getInt(0, Typeface.NORMAL);
            androidAttrs.recycle();

            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceButton, android.R.attr.buttonStyle, 0);
            int typefaceEnum = styledAttrs.getInt(R.styleable.TypefaceButton_typeface, TypefaceManager.FOO_FONT.attributeValue);
            styledAttrs.recycle();

            mTypefaceManager.applyTypeface(this, typefaceEnum, textStyle);
        }
    }
}
