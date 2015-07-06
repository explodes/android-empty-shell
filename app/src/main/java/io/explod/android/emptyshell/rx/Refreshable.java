package io.explod.android.emptyshell.rx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

public abstract class Refreshable<R, T> {

	protected static class InternalObserver<T> implements Observer<T> {
		private Subject<T, T> mSubject;

		public InternalObserver(Subject<T, T> subject) {
			mSubject = subject;
		}

		@Override
		public void onCompleted() {
			// internalObserver's job is to skip onComplete events.
		}

		@Override
		public void onError(Throwable e) {
			mSubject.onError(e);
		}

		@Override
		public void onNext(T t) {
			mSubject.onNext(t);
		}
	}

	/**
	 * Holds subscriptions based per parameters.
	 */
	protected Map<R, Subscription> mSourceSubscriptions = new HashMap<>();

	// internal observation
	protected final Map<R, BehaviorSubject<T>> mSubjects = Collections.synchronizedMap(new HashMap<R, BehaviorSubject<T>>(1));
	protected final Map<R, Observer<T>> mInternalObservers = Collections.synchronizedMap(new HashMap<R, Observer<T>>(1));

	/**
	 * Get the Subject responsible for delegating internal observer results for specific parameters.
	 */
	protected synchronized BehaviorSubject<T> getSubject(R parameters) {
		BehaviorSubject<T> subject = mSubjects.get(parameters);
		if (subject == null) {
			subject = createSubject();
			mSubjects.put(parameters, subject);
		}
		return subject;
	}

	/**
	 * Create a new subject for given paramters
	 */
	protected BehaviorSubject<T> createSubject() {
		return BehaviorSubject.create();
	}

	/**
	 * Get the internal Observer responsible for delegating observer results for specific parameters
	 */
	protected synchronized Observer<T> getInternalObserver(R parameters) {
		Observer<T> internalObserver = mInternalObservers.get(parameters);
		if (internalObserver == null) {
			internalObserver = createInternalObserver(getSubject(parameters));
			mInternalObservers.put(parameters, internalObserver);
		}
		return internalObserver;
	}

	/**
	 * Create a new internal observer for a given set of parameters. By contract, it should never delegate onComplete but it must delegate
	 * onError and onNext to the subject.
	 */
	protected Observer<T> createInternalObserver(final Subject<T, T> subject) {
		return new InternalObserver<>(subject);
	}

	/**
	 * Return the Observable that gets data from an original source, such as a network or database. Useful with Retrofit, where Observables
	 * are returned.
	 */
	protected abstract Observable<T> getSourceObservable(R parameters);

	/**
	 * @return Cache-enabled Observable without providing parameters.
	 */
	public Observable<T> getObservable() {
		return getObservable(null);
	}

	/**
	 * Provides an {@link Observable} that will never call {@link Observer#onCompleted() Observer#onComplete}, leaving the subscription
	 * open all the time. <p>This allows all observers to always be subscribed, even if the underlying {@link Observable} changes (e.g.
	 * switch from a network request to cached data).
	 *
	 * @param parameters parameters Parameters for the action being performed by this Refreshable. This is used as a key to get the proper
	 *                   {@link Observable} from a map of possible options.
	 */
	public Observable<T> getObservable(R parameters) {
		Subscription subscription = mSourceSubscriptions.get(parameters);
		if (subscription == null || subscription.isUnsubscribed()) {
			// Subscribe to server
			updateSubscription(parameters);
		}
		return getSubject(parameters);
	}

	/**
	 * Use the original source Observer to refresh data and send it down the pipeline.
	 */
	public void refreshFromSource() {
		unsubscribe(null);
		updateSubscription(null);
	}

	/**
	 * Use the original source Observer to refresh data and send it down the pipeline.
	 */
	public void refreshFromSource(R parameters) {
		unsubscribe(parameters);
		updateSubscription(parameters);
	}

	protected void unsubscribe(R parameters) {
		final Subscription subscription = mSourceSubscriptions.get(parameters);
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
			mSourceSubscriptions.remove(parameters);
		}
	}

	protected void updateSubscription(R parameters) {
		Observer<T> internalObserver = getInternalObserver(parameters);
		Subscription subscription = getSourceObservable(parameters)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(internalObserver);
		mSourceSubscriptions.put(parameters, subscription);
	}

	public String getName() {
		return getClass().getName();
	}

	@Override
	public String toString() {
		return getName();
	}

}
