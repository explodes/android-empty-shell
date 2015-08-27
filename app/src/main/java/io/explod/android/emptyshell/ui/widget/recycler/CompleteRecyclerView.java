package io.explod.android.emptyshell.ui.widget.recycler;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.explod.android.emptyshell.R;

public class CompleteRecyclerView extends FrameLayout {

	private static final int MODE_LOADING = 0;

	private static final int MODE_EMPTY = 1;

	private static final int MODE_LIST = 2;

	@IntDef({MODE_LOADING, MODE_EMPTY, MODE_LIST})
	@Retention(RetentionPolicy.SOURCE)
	public @interface DisplayMode {
	}

	@DisplayMode
	private int mMode = MODE_LOADING;

	private int mLoaded = -1;

	@Bind(R.id.inner_list)
	RecyclerView mRecyclerView;

	@Nullable
	@Bind(R.id.empty)
	View mEmptyView;

	@Nullable
	@Bind(R.id.loading)
	View mLoadingView;

	@Bind(R.id.refresher)
	SwipeRefreshLayout mSwipeRefreshLayout;

	CompleteRecyclerViewAdapter.LoadingAdapterDataObserver mObserver = new CompleteRecyclerViewAdapter.LoadingAdapterDataObserver() {
		@Override
		public void onChanged() {
			updateDisplayedView();
		}

		@Override
		public void onLoaded(int numItems) {
			setLoaded(numItems);
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

	public void setRecyclerView(@NonNull RecyclerView recyclerView) {
		if (mRecyclerView != null) {
			removeView(mRecyclerView);
		}
		mRecyclerView = recyclerView;
		addView(recyclerView);
		showMode();
	}

	@Nullable
	public View getEmptyView() {
		return mEmptyView;
	}

	public void setEmptyView(@Nullable View emptyView) {
		if (mEmptyView != null) {
			removeView(mEmptyView);
		}
		mEmptyView = emptyView;
		if (emptyView != null) {
			addView(emptyView);
		}
		showMode();
	}

	@Nullable
	public View getLoadingView() {
		return mLoadingView;
	}

	public void setLoadingView(@Nullable View loadingView) {
		if (mLoadingView != null) {
			removeView(mLoadingView);
		}
		mLoadingView = loadingView;
		if (loadingView != null) {
			addView(loadingView);
		}
		showMode();
	}

	public void setLoaded(int numItems) {
		mLoaded = numItems;
		updateDisplayedView();
	}

	public void showRecyclerView() {
		showMode(MODE_LIST);
	}

	public void showEmptyView() {
		showMode(MODE_EMPTY);
	}

	public void showLoadingView() {
		showMode(MODE_LOADING);
	}

	private void showMode(@DisplayMode int mode) {
		mMode = mode;
		showMode();
	}

	private void showMode() {
		boolean loading = false;
		boolean empty = false;
		boolean recycler = true;

		switch (mMode) {
			case MODE_EMPTY:
				if (mEmptyView != null) {
					empty = true;
					recycler = false;
				}
				break;
			case MODE_LOADING:
				if (mLoadingView != null) {
					loading = true;
					recycler = false;
				}
				break;
		}

		if (mLoadingView != null) {
			mLoadingView.setVisibility(loading ? VISIBLE : GONE);
		}
		if (mEmptyView != null) {
			mEmptyView.setVisibility(empty ? VISIBLE : GONE);
		}
		if (mRecyclerView != null) {
			mRecyclerView.setVisibility(recycler ? VISIBLE : GONE);
		}
	}

	private void updateDisplayedView() {
		if (mLoaded < 0) {
			showLoadingView();
		} else if (mLoaded == 0) {
			showEmptyView();
		} else {
			showRecyclerView();
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

	public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
		getRecyclerView().setLayoutManager(layoutManager);
	}
}
