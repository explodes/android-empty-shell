package io.explod.android.emptyshell.util.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class TypefaceManager {

    // ===== Fonts ======
    public static final CustomTypeface FOO_FONT = new CustomTypeface("foo.ttf", 0);
    // ==================

    private static CustomTypeface getTypefaceForAttributeValue(int typefaceAttributeValue) {
        switch (typefaceAttributeValue) {
            case 0:
                return FOO_FONT;
            default:
                throw new IllegalArgumentException(typefaceAttributeValue + " is not a valid typeface attribute value. This must match the values in TypefaceManager.");
        }
    }

    public TypefaceManager() {

    }

    public void applyTypeface(TextView view, int typefaceAttributeValue, int textStyle) {
        applyTypeface(view, getTypefaceForAttributeValue(typefaceAttributeValue), textStyle);
    }

    public void applyTypeface(TextView view, CustomTypeface typeface) {
        applyTypeface(view, typeface, Typeface.NORMAL);
    }

    public void applyTypeface(TextView view, CustomTypeface customTypeface, int textStyle) {
        Typeface typeface = get(view.getContext(), customTypeface);
        if (typeface != null) view.setTypeface(typeface);
    }

    /**
     * Lazy loads and caches the requested typeface.
     *
     * @param context                Context used to load typefaces
     * @param typefaceAttributeValue Value representing the typeface to load (defined in res/attrs.xml and {@link TypefaceManager TypefaceManager})
     * @return The {@link Typeface} requested, or null if it could not be found.
     */
    public Typeface get(Context context, int typefaceAttributeValue) {
        return get(context, getTypefaceForAttributeValue(typefaceAttributeValue));
    }

    /**
     * Lazy loads and caches the requested typeface.
     *
     * @param context        Context used to load typeface.
     * @param customTypeface Typeface to load.
     * @return The {@link Typeface} requested, or null if it could not be found.
     */
    public Typeface get(Context context, CustomTypeface customTypeface) {
        if (customTypeface == null)
            throw new IllegalArgumentException("Unknown 'typeface' attribute");

        if (customTypeface.getTypeface() == null) {
            // Log.d("TypefaceManager", "Typeface cache miss for '" + t.fontFileName + "', creating from asset.");
            try {
                customTypeface.create(context);
            } catch (Exception e) {
                Log.e("TypefaceManager", "Could create typeface '" + customTypeface.fontFileName + "' because " + e.getMessage(), e);
            }
        }
        return customTypeface.getTypeface();
    }


}