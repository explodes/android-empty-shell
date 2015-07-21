package io.explod.android.emptyshell.ui.widget.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import javax.inject.Inject;

import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

import static io.explod.android.emptyshell.App.getApp;

public class TypefaceButton extends Button {

	@Inject
	TypefaceManager mTypefaceManager;

	public TypefaceButton(Context context) {
		super(context);
	}

	public TypefaceButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadAttrs(context, attrs);
	}

	public TypefaceButton(Context context, AttributeSet attrs, int defStyle) {
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
