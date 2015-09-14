package io.explod.android.emptyshell.util.rx;

import rx.Observer;

/**
 * Wrapper for an Observer that forwards onNext and onError but NOT onComplete.
 * <p>
 * It is useful for sinking to a Subject that you do not want to complete.
 *
 * @param <T> the type of item the Observer expects to observe
 */
public class NoCompleteWrapper<T> implements Observer<T> {

	private Observer<T> mObserver;

	public NoCompleteWrapper(Observer<T> observer) {
		mObserver = observer;
	}

	@Override
	public void onCompleted() {
		// ignore this event
	}

	@Override
	public void onNext(T t) {
		mObserver.onNext(t);
	}

	@Override
	public void onError(Throwable e) {
		mObserver.onError(e);
	}
}