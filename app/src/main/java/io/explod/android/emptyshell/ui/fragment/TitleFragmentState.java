package io.explod.android.emptyshell.ui.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import io.explod.android.emptyshell.ui.activity.BaseActivity;

public class TitleFragmentState extends FragmentState {

	@StringRes
	private int mTitleRes;

	@StringRes
	private int mSubtitleRes;

	public TitleFragmentState(@Nullable String tag, @StringRes int titleRes, @StringRes int subtitleRes) {
		super(tag);
		mTitleRes = titleRes;
		mSubtitleRes = subtitleRes;
	}

	public int getTitleRes() {
		return mTitleRes;
	}

	public void setTitleRes(int titleRes) {
		mTitleRes = titleRes;
	}

	public int getSubtitleRes() {
		return mSubtitleRes;
	}

	public void setSubtitleRes(int subtitleRes) {
		mSubtitleRes = subtitleRes;
	}

	@Override
	public void setTo(@NonNull BaseActivity activity) {
		if (mTitleRes == 0) {
			activity.setTitle("");
		} else {
			activity.setTitle(mTitleRes);
		}
		if (mSubtitleRes == 0) {
			activity.setSubtitle("");
		} else {
			activity.setSubtitle(mSubtitleRes);
		}
	}
}
