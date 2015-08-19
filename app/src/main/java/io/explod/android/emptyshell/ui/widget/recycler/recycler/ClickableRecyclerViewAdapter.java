package io.explod.android.emptyshell.ui.widget.recycler.recycler;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ClickableRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	@Nullable
	private View.OnClickListener mOnClickListener;

	@IdRes
	private int mPositionTag;

	/**
	 * Create an adapter that does not report item clicks
	 */
	public ClickableRecyclerViewAdapter() {
		this(null, 0);
	}

	/**
	 * Create an adapter that reports item clicks.
	 *
	 * @param onClickListener Click listener that responds to item clicks in this adapter
	 */
	public ClickableRecyclerViewAdapter(@Nullable View.OnClickListener onClickListener) {
		this(onClickListener, 0);
	}

	/**
	 * Create an adapter that reports item clicks.
	 *
	 * @param onClickListener Click listener that responds to item clicks in this adapter
	 * @param positionTag     0 or non-zero, optional id for the tag to put the item position in
	 */
	public ClickableRecyclerViewAdapter(@Nullable View.OnClickListener onClickListener, @IdRes int positionTag) {
		mOnClickListener = onClickListener;
		mPositionTag = positionTag;
	}

	public void setOnClickListener(@Nullable View.OnClickListener onClickListener) {
		mOnClickListener = onClickListener;
		notifyDataSetChanged();
	}

	public void setPositionTag(@IdRes int positionTag) {
		mPositionTag = positionTag;
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(VH holder, int position) {
		if (mOnClickListener != null) {
			holder.itemView.setOnClickListener(mOnClickListener);
		}
		if (mPositionTag != 0) {
			holder.itemView.setTag(mPositionTag, position);
		}
		onBindClickableViewHolder(holder, position);
	}

	public abstract void onBindClickableViewHolder(VH holder, int position);

}
