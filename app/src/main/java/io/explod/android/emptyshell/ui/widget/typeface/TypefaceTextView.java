package io.explod.android.emptyshell.ui.widget.typeface;

import android.content.Context;
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

	public TypefaceTextView(Context context) {
		super(context);
	}

	public TypefaceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadAttrs(context, attrs);
	}

	public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		loadAttrs(context, attrs);
	}

	private void loadAttrs(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			getApp().getObjectGraph().inject(this);
			Typeface typeface = mTypefaceManager.getTypeface(context, attrs, R.styleable.TypefaceButton, R.styleable.TypefaceButton_typeface);
			if (typeface != null) {
				setTypeface(typeface);
			}
		}
	}
}
