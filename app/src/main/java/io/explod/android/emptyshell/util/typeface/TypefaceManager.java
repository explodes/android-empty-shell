package io.explod.android.emptyshell.util.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.HashMap;

public class TypefaceManager {

	public HashMap<TypefaceDescription, Typeface> mTypefaceCache = new HashMap<>(TypefaceDescription.values().length);

	public Typeface getTypeface(Context context, int attributeId) {
		for (TypefaceDescription typefaceDescription : TypefaceDescription.values()) {
			if (typefaceDescription.attributeId == attributeId) {
				return getTypeface(context, typefaceDescription);
			}
		}
		return null;
	}

	public Typeface getTypeface(Context context, TypefaceDescription typefaceDescription) {
		Typeface typeface;
		if (mTypefaceCache.containsKey(typefaceDescription)) {
			typeface = mTypefaceCache.get(typefaceDescription);
		} else {
			typeface = typefaceDescription.loadTypeface(context);
			mTypefaceCache.put(typefaceDescription, typeface);
		}
		return typeface;
	}

	public Typeface getTypeface(Context context, AttributeSet attrSet, int[] attrs, int index) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrSet, attrs, 0, 0);
		int typefaceId = -1;
		try {
			typefaceId = a.getInteger(index, -1);
		} finally {
			a.recycle();
		}
		if (typefaceId != -1) {
			return getTypeface(context, typefaceId);
		} else {
			return null;
		}
	}

}
