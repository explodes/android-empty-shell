package io.explod.android.emptyshell.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;

import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.ui.activity.BaseActivity;
import io.explod.android.emptyshell.ui.dialog.HasDialogs;
import io.explod.android.emptyshell.ui.dialog.HasToast;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static io.explod.android.emptyshell.App.logException;

public abstract class BaseFragment extends RxFragment implements HasDialogs, HasToast {

	private static final String TAG = BaseFragment.class.getSimpleName();

	private ProgressDialog mProgressDialog;

	private AlertDialog mAlertDialog;

	@Override
	public void onPause() {
		hideDialogs();
		super.onPause();
	}

	@Override
	public void showAlertDialog(@StringRes int titleResId, @StringRes int messageResId) {
		hideDialogs();
		Context context = getContext();
		if (context == null) {
			return;
		}
		mAlertDialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, null)
			.setTitle(titleResId)
			.setMessage(messageResId)
			.create();
		mAlertDialog.show();
	}

	@Override
	public void showAlertDialog(@StringRes int messageResId) {
		hideDialogs();
		Context context = getContext();
		if (context == null) {
			return;
		}
		mAlertDialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, null)
			.setMessage(messageResId)
			.create();
		mAlertDialog.show();
	}

	@Override
	public void showProgressDialog(@StringRes int titleResId, @StringRes int messageResId) {
		hideDialogs();
		Context context = getContext();
		if (context == null) {
			return;
		}
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle(titleResId);
		mProgressDialog.setMessage(context.getText(messageResId));
		mProgressDialog.show();
	}

	@Override
	public void showProgressDialog(@StringRes int messageResId) {
		hideDialogs();
		Context context = getContext();
		if (context == null) {
			return;
		}
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage(context.getText(messageResId));
		mProgressDialog.show();
	}

	@Override
	public void hideDialogs() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mProgressDialog = null;
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
		mAlertDialog = null;
	}

	@Override
	public void toastShort(@StringRes int messageResId) {
		toast(messageResId, Toast.LENGTH_SHORT);
	}

	@Override
	public void toastLong(@StringRes int messageResId) {
		toast(messageResId, Toast.LENGTH_LONG);
	}

	private void toast(@StringRes int messageResId, int duration) {
		Context context = getActivity();
		if (context != null) {
			Toast.makeText(context, messageResId, duration).show();
		}

	}

	/**
	 * Show an "Unexpected Error" alert and log the exception.
	 *
	 * @param error Exception to log
	 */
	public void showUnexpectedErrorDialog(Throwable error) {
		Log.e(TAG, "unexpected error", error);
		logException(error);
		showUnexpectedErrorDialog();
	}

	public void showUnexpectedErrorDialog() {
		showAlertDialog(R.string.dialog_unexpected_error);
	}

	// Activity calls - shortcuts to methods on Activity or BaseActivity

	public void openFragment(FullFragment fragment, boolean append) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return;
		}
		activity.openFragment(fragment, append);
	}

	public void setTitle(CharSequence title) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity != null) {
			activity.setTitle(title);
		}
	}

	public void setTitle(@StringRes int titleRes) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity != null) {
			activity.setTitle(titleRes);
		}
	}

	public void setSubtitle(CharSequence subtitle) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity != null) {
			activity.setSubtitle(subtitle);
		}
	}

	public void setSubtitle(@StringRes int subtitleRes) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity != null) {
			activity.setSubtitle(subtitleRes);
		}
	}

	public void throwNotUsingNewInstance() {
		throw new IllegalStateException("always use newInstance when constructing fragments");
	}

	protected <T> Observable<T> bind(Observable<T> observable) {
		return observable
			.compose(bindToLifecycle())
			.observeOn(AndroidSchedulers.mainThread());
	}
}
