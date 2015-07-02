package io.explod.android.emptyshell.rx;


import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public abstract class CachedRefreshable<R, T> extends Refreshable<R, T> {

    public abstract Observable<T> getSourceObservable(R parameters);

    /**
     * Return the Observable that gets data from a cached source.
     *
     * @return Observable from cache item, or null if the cache misses.
     */
    public abstract Observable<T> getCachedObservable(R parameters);

    /**
     * When source data is acquired, this function will be used as a callback so that the acquired data can be cached.
     */
    protected abstract void updateCache(R parameters, T data);

    /**
     * When data comes from a miscellaneous source, this provides a mechanism to broadcast those changes to subscribers.
     */
    public void setValue(R parameters, T data) {
        updateCache(parameters, data);
        getInternalObserver(parameters).onNext(data);
    }

    /**
     * @return Cache-enabled Observable without providing parameters.
     */
    public Observable<T> getObservable() {
        return getObservable(null);
    }

    /**
     * @param parameters Parameters for the action being observed by this Refreshable.
     * @return Cache-enabled Observable with the specified parameters.
     */
    public Observable<T> getObservable(R parameters) {
        return getObservable(parameters, true);
    }

    /**
     * Provides an {@link Observable} that will never call {@link Observer#onCompleted() Observer#onComplete}, leaving the subscription
     * open all the time. <p>This allows all observers to always be subscribed, even if the underlying {@link Observable} changes (e.g.
     * network request or cached data).
     *
     * @param parameters parameters Parameters for the action being performed by this Refreshable. This is used as a key to get the proper
     *                   {@link Observable} from a map of possible options.
     * @param allowCache Whether to check for a cached copy of the data being observed.
     */
    public Observable<T> getObservable(R parameters, boolean allowCache) {
        if (!allowCache) {
            updateCache(parameters, null);
            refreshFromSource(parameters);
        }

        Observable<T> cachedObservable = getCachedObservable(parameters);
        if (allowCache && cachedObservable != null) {
            // Use the cache
            final Observer<T> internalObserver = getInternalObserver(parameters);
            cachedObservable.subscribe(internalObserver);
        } else {
            Observable<T> obs = super.getObservable(parameters);
            if (!allowCache && getSubject(parameters).hasValue()) {
                // BehaviorSubject has a cached value, skip it and wait for the value from the server.
                obs = obs.skip(1);
            }
            return obs;

        }
        return getSubject(parameters);
    }

    @Override
    protected void updateSubscription(final R parameters) {
        final Observer<T> internalObserver = getInternalObserver(parameters);
        final Subscription subscription = getSourceObservable(parameters)
            .doOnNext(new Action1<T>() {
                @Override
                public void call(T t) {
                    updateCache(parameters, t);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(internalObserver);
        mSourceSubscriptions.put(parameters, subscription);
    }

}
