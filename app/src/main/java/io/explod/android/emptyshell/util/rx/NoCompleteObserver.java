package io.explod.android.emptyshell.util.rx;

import rx.Observer;

/**
 * {@link Observer} used in cases where {@link #onCompleted()} is not called
 *
 * @param <T> Type of items being emitted
 */
public abstract class NoCompleteObserver<T> implements Observer<T> {

	@Override
	public void onCompleted() {
		// ignore this event
	}

}
