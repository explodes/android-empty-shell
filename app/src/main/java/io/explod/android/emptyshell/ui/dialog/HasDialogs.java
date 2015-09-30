package io.explod.android.emptyshell.ui.dialog;

import android.support.annotation.StringRes;

public interface HasDialogs {

	void showProgressDialog(@StringRes int titleResId, @StringRes int messageResId);

	void showProgressDialog(@StringRes int messageResId);

	void showAlertDialog(@StringRes int titleResId, @StringRes int messageResId);

	void showAlertDialog(@StringRes int messageResId);

	void hideDialogs();
}
