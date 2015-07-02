package io.explod.android.emptyshell.ui.widget.recycler;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class CompleteRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private boolean mLoaded = false;

    public static abstract class LoadableAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        public abstract void onLoaded(boolean loaded);
    }

    private final List<LoadableAdapterDataObserver> mLoadableAdapterDataObservers = new ArrayList<>();

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (observer instanceof LoadableAdapterDataObserver) {
            LoadableAdapterDataObserver loadableAdapterDataObserver = (LoadableAdapterDataObserver) observer;
            mLoadableAdapterDataObservers.add(loadableAdapterDataObserver);
            if (mLoaded) {
                loadableAdapterDataObserver.onLoaded(true);
            }
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (observer instanceof LoadableAdapterDataObserver) {
            mLoadableAdapterDataObservers.remove(observer);
        }
    }

    public void setLoaded(boolean loaded) {
        if (mLoaded != loaded) {
            mLoaded = loaded;
            for (LoadableAdapterDataObserver observer : mLoadableAdapterDataObservers) {
                observer.onLoaded(loaded);
            }
        }
    }
}
