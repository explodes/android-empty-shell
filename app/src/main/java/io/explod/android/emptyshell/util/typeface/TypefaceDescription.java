package io.explod.android.emptyshell.util.typeface;

import android.content.Context;
import android.graphics.Typeface;

public enum TypefaceDescription {
	FOO("fonts/foo.ttf", 1);

	String assetPath;
	int attributeId;

	TypefaceDescription(String assetPath, int attributeId) {
		this.assetPath = assetPath;
		this.attributeId = attributeId;
	}

	public Typeface loadTypeface(Context context) {
		return Typeface.createFromAsset(context.getAssets(), assetPath);
	}
}
