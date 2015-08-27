package io.explod.android.emptyshell.ui.widget.recycler;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class CompleteRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	private int mLoaded = -1;

	public static abstract class LoadingAdapterDataObserver extends RecyclerView.AdapterDataObserver {
		public abstract void onLoaded(int numItems);
	}

	private final List<LoadingAdapterDataObserver> mLoadingAdapterDataObservers = new ArrayList<>();

	@Override
	public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
		super.registerAdapterDataObserver(observer);
		if (observer instanceof LoadingAdapterDataObserver) {
			LoadingAdapterDataObserver loadingAdapterDataObserver = (LoadingAdapterDataObserver) observer;
			mLoadingAdapterDataObservers.add(loadingAdapterDataObserver);
			loadingAdapterDataObserver.onLoaded(mLoaded);
		}
	}

	@Override
	public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
		super.unregisterAdapterDataObserver(observer);
		if (observer instanceof LoadingAdapterDataObserver) {
			mLoadingAdapterDataObservers.remove(observer);
		}
	}

	/**
	 * Set the number of tangible elements in the adapter
	 */
	public void setLoaded(int loaded) {
		mLoaded = loaded;
		for (LoadingAdapterDataObserver observer : mLoadingAdapterDataObservers) {
			observer.onLoaded(loaded);
		}
	}

}
