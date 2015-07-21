package io.explod.android.emptyshell.rx;

import rx.Observable;
import rx.Observer;
import rx.subjects.BehaviorSubject;

/**
 * Delegate for Rx events.
 * <p/>
 * Think of this as kind of a Publisher that specializes in RxEvents.
 * <p/>
 * Events from other sources are forwarded from this object and the last of
 * those items are forwarded to new subscribers (callbacks).
 * <p/>
 * One use for a delegate is to have network IO that doesn't get interrupted by
 * screen rotation or other configuration changes.
 * <p/>
 * Delegates do NOT forward an onComplete(). You make subscribe with a {@link NoCompleteObserver}
 *
 * @param <T> type of items this delegate will emit
 */
public abstract class RxDelegate<T> {

	/**
	 * Subject the sink published
	 */
	private BehaviorSubject<T> mSubject = BehaviorSubject.create();

	/**
	 * Sink that delegate functions should emit items to.
	 * <p/>
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
	 * Get the item to which inner Observables should subscribe
	 * <p/>
	 * Example:
	 * <pre>
	 * private void search(String query) {
	 *     mApiaryService
	 *         .search(query)
	 *         .subscribe(getSink());
	 * }
	 * </pre>
	 */
	protected Observer<T> getSink() {
		return mSink;
	}

	/**
	 * Observable to subscribe to this delegate's events from.
	 * <p/>
	 * Delegates do NOT forward an onComplete(). You make subscribe with a {@link NoCompleteObserver}
	 */
	public Observable<T> getObservable() {
		return mSubject;
	}

}
