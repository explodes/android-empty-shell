package io.explod.android.emptyshell.ui.widget.typeface;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import javax.inject.Inject;

import io.explod.android.emptyshell.util.typeface.TypefaceDescription;
import io.explod.android.emptyshell.util.typeface.TypefaceManager;

import static io.explod.android.emptyshell.App.getApp;


public class CustomTypefaceSpan extends TypefaceSpan {

	@Inject
	TypefaceManager mTypefaceManager;

	private final Typeface mTypeface;

	public CustomTypefaceSpan(String family, Typeface typeface) {
		super(family);
		mTypeface = typeface;
	}

	public CustomTypefaceSpan(Context context, String family, TypefaceDescription typeface) {
		super(family);
		getApp(context).getObjectGraph().inject(this);
		mTypeface = mTypefaceManager.getTypeface(context, typeface);
	}

	@Override
	public void updateDrawState(@NonNull TextPaint ds) {
		applyCustomTypeFace(ds, mTypeface);
	}

	@Override
	public void updateMeasureState(@NonNull TextPaint paint) {
		applyCustomTypeFace(paint, mTypeface);
	}

	private static void applyCustomTypeFace(Paint paint, Typeface tf) {
		int oldStyle;
		Typeface old = paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}
		int fake = oldStyle & ~tf.getStyle();
		if ((fake & Typeface.BOLD) != 0) {
			paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			paint.setTextSkewX(-0.25f);
		}

		paint.setTypeface(tf);
	}
}