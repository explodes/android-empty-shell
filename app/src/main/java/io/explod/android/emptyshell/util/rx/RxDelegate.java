package io.explod.android.emptyshell.util.rx;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Observer;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

/**
 * Delegate for Rx events.
 * <p>
 * Think of this as kind of a Publisher that specializes in RxEvents.
 * <p>
 * Events from other sources are forwarded from this object and the last of
 * those items are forwarded to new subscribers (callbacks).
 * <p>
 * One use for a delegate is to have network IO that doesn't get interrupted by
 * screen rotation or other configuration changes.
 * <p>
 * Delegates do NOT forward an onComplete(). You make subscribe with a {@link NoCompleteObserver}
 *
 * @param <T> type of items this delegate will emit
 */
public abstract class RxDelegate<T> {

	/**
	 * Subject the sink publishes to
	 */
	private Subject<T, T> mSubject;

	/**
	 * Sink that delegate functions should emit items to.
	 * <p>
	 * Example:
	 * <pre>
	 * private void search(String query) {
	 *     mApiaryService
	 *         .search(query)
	 *         .subscribe(getSink());
	 * }
	 * </pre>
	 */
	private Observer<T> mSink = new NoCompleteWrapper<>(mSubject);

	/**
	 * Construct this beauty
	 */
	public RxDelegate() {
		mSubject = initializeSubject();
	}

	/**
	 * Create the default subject for publishing events and data
	 */
	@NonNull
	protected Subject<T, T> initializeSubject() {
		return BehaviorSubject.create();
	}

	/**
	 * Get the item to which inner Observables should subscribe
	 * <p>
	 * Example:
	 * <pre>
	 * private void search(String query) {
	 *     mApiaryService
	 *         .search(query)
	 *         .subscribe(getSink());
	 * }
	 * </pre>
	 */
	@NonNull
	protected Observer<T> getSink() {
		return mSink;
	}

	/**
	 * Observable to subscribe to this delegate's events from.
	 * <p>
	 * Delegates do NOT forward an onComplete(). You make subscribe with a {@link NoCompleteObserver}
	 */
	@NonNull
	public Observable<T> getObservable() {
		return mSubject;
	}

}
