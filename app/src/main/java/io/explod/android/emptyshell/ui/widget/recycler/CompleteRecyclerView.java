package io.explod.android.emptyshell.ui.widget.recycler;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.explod.android.emptyshell.R;

public class CompleteRecyclerView extends FrameLayout {

	private static final int MODE_LOADING = 0;

	private static final int MODE_EMPTY = 1;

	private static final int MODE_LIST = 2;

	private int mMode = MODE_LOADING;

	private boolean mLoaded = false;

	@Bind(R.id.inner_list)
	RecyclerView mRecyclerView;

	@Bind(R.id.empty)
	View mEmptyView;

	@Bind(R.id.loading)
	View mLoadingView;

	@Bind(R.id.refresher)
	SwipeRefreshLayout mSwipeRefreshLayout;

	CompleteRecyclerViewAdapter.LoadableAdapterDataObserver mObserver = new CompleteRecyclerViewAdapter.LoadableAdapterDataObserver() {
		@Override
		public void onChanged() {
			updateDisplayedView();
		}

		@Override
		public void onLoaded(boolean loaded) {
			setLoaded(loaded);
		}

	};

	public CompleteRecyclerView(Context context) {
		super(context);
		start(context);
	}

	public CompleteRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		start(context);
	}

	public CompleteRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		start(context);
	}

	private void start(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_complete_recycler_view, this);
		ButterKnife.bind(this);
		mSwipeRefreshLayout.setEnabled(false);
	}

	public SwipeRefreshLayout getSwipeRefreshLayout() {
		return mSwipeRefreshLayout;
	}

	public RecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	public void setRecyclerView(RecyclerView recyclerView) {
		if (mRecyclerView != null) {
			removeView(mRecyclerView);
		}
		mRecyclerView = recyclerView;
		if (recyclerView != null) {
			addView(recyclerView);
		}
		showMode();
	}

	public View getEmptyView() {
		return mEmptyView;
	}

	public void setEmptyView(View emptyView) {
		if (mEmptyView != null) {
			removeView(mEmptyView);
		}
		mEmptyView = emptyView;
		if (emptyView != null) {
			addView(emptyView);
		}
		showMode();
	}

	public View getLoadingView() {
		return mLoadingView;
	}

	public void setLoadingView(View loadingView) {
		if (mLoadingView != null) {
			removeView(mLoadingView);
		}
		mLoadingView = loadingView;
		if (loadingView != null) {
			addView(loadingView);
		}
		showMode();
	}

	public void setLoaded(boolean loaded) {
		mLoaded = loaded;
		updateDisplayedView();
	}

	public void showListView() {
		showMode(MODE_LIST);
	}

	public void showEmptyView() {
		showMode(MODE_EMPTY);
	}

	public void showLoadingView() {
		showMode(MODE_LOADING);
	}

	private void showMode(int mode) {
		mMode = mode;
		showMode();
	}

	private void showMode() {
		int mode = mMode; // read mode once to avoid weird async glitches
		if (mLoadingView != null) {
			mLoadingView.setVisibility(mode == MODE_LOADING ? VISIBLE : GONE);
		}
		if (mEmptyView != null) {
			mEmptyView.setVisibility(mode == MODE_EMPTY ? VISIBLE : GONE);
		}
		if (mRecyclerView != null) {
			mRecyclerView.setVisibility(mode == MODE_LIST ? VISIBLE : GONE);
		}
	}

	private void updateDisplayedView() {
		if (mLoaded) {
			// empty or list
			RecyclerView.Adapter adapter = mRecyclerView == null ? null : mRecyclerView.getAdapter();
			if (adapter == null || adapter.getItemCount() == 0) {
				showEmptyView();
			} else {
				showListView();
			}
		} else {
			showLoadingView();
		}
	}

	private void unregisterObserver() {
		if (mRecyclerView != null) {
			RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
			if (adapter != null) {
				adapter.unregisterAdapterDataObserver(mObserver);
			}
		}
	}

	private void registerObserver() {
		if (mRecyclerView != null) {
			RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
			if (adapter != null) {
				adapter.registerAdapterDataObserver(mObserver);
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		unregisterObserver();
		super.onDetachedFromWindow();
	}

	public void setAdapter(RecyclerView.Adapter adapter) {
		unregisterObserver();
		mRecyclerView.setAdapter(adapter);
		registerObserver();
	}
}
