package io.explod.android.emptyshell.util;

import android.view.View;
import android.view.ViewTreeObserver;

public class LayoutUtil {

    /**
     * Listener for size availability
     */
    public interface SizeAvailableListener {
        /**
         * Fires when size is available
         */
        void onSizeAvailable();
    }

    /**
     * When you absolutely need a size of a view. Fires the listener immediately if the view size is available, otherwise, it waits until the end of the initial layout. This will only give you the first available size. The listener only fires up to one time.
     *
     * @param view     View to watch for a size
     * @param listener Listener to fire when size is available
     */
    public static void OnSizeAvailable(final View view, final SizeAvailableListener listener) {
        // if the size is already available
        if (view.getHeight() != 0 || view.getWidth() != 0) {
            // fire the listener
            listener.onSizeAvailable();
        } else {
            // wait for the view to lay out
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // do not listen to events twice
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    // size is available
                    listener.onSizeAvailable();
                }
            });
        }
    }
}
