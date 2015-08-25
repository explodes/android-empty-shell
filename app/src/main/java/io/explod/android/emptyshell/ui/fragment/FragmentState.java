package io.explod.android.emptyshell.ui.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import io.explod.android.emptyshell.ui.activity.BaseActivity;

public abstract class FragmentState implements Serializable {

	@Nullable
	String mTag;

	public FragmentState(@Nullable String tag) {
		mTag = tag;
	}

	@Nullable
	public String getTag() {
		return mTag;
	}

	public abstract void setTo(@NonNull BaseActivity activity);

}
