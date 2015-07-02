package io.explod.android.emptyshell.util.typeface;

import android.content.Context;
import android.graphics.Typeface;

public class CustomTypeface {
    private static final String FONT_FILE_PATH = "fonts/%s";
    public final String fontFileName;
    public final int attributeValue;
    private Typeface typeface = null;

    public CustomTypeface(String fontFileName, int attributeValue) {
        this.fontFileName = fontFileName;
        this.attributeValue = attributeValue;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void create(Context c) {
        // Log.d("CustomTypeface", String.format(FONT_FILE_PATH, fontFileName));
        typeface = Typeface.createFromAsset(c.getAssets(), String.format(FONT_FILE_PATH, fontFileName));
    }
}