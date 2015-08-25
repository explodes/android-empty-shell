package io.explod.android.emptyshell.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.explod.android.emptyshell.BuildConfig;
import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.ui.activity.BaseActivity;
import io.explod.android.emptyshell.ui.dialog.HasDialogs;
import io.explod.android.emptyshell.ui.dialog.HasToast;
import rx.Observable;

import static rx.android.app.AppObservable.bindFragment;

public abstract class BaseFragment extends Fragment implements HasDialogs, HasToast {

	private static final String TAG = BaseFragment.class.getSimpleName();

	private ProgressDialog mProgressDialog;

	private AlertDialog mAlertDialog;

	@Override
	public void onPause() {
		hideDialogs();
		super.onPause();
	}

	@Override
	public void showAlertDialog(@StringRes int messageResId) {
		showAlertDialog(getResources().getText(messageResId));
	}

	@Override
	public void showAlertDialog(CharSequence message) {
		hideDialogs();
		if (mAlertDialog == null) {
			Context context = getActivity();
			if (context == null) {
				return;
			}
			mAlertDialog = new AlertDialog.Builder(context)
				.setPositiveButton(android.R.string.ok, null)
				.create();

		}
		mAlertDialog.setMessage(message);
		mAlertDialog.show();
	}

	@Override
	public void showProgressDialog(@StringRes int messageResId) {
		showProgressDialog(getResources().getText(messageResId));
	}

	@Override
	public void showProgressDialog(CharSequence message) {
		hideDialogs();
		if (mProgressDialog == null) {
			Context context = getActivity();
			if (context == null) {
				return;
			}
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setCancelable(false);
		}
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
	}

	@Override
	public void hideDialogs() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
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

	public void showUnexpectedErrorDialog(Throwable error, boolean log) {
		Log.e(TAG, "unexpected error", error);
		if (log) {
			logException(error);
		}
		showUnexpectedErrorDialog();
	}

	public void showUnexpectedErrorDialog() {
		showAlertDialog(R.string.dialog_unexpected_error);
	}

	protected void logException(Throwable error) {
		if (!BuildConfig.DEBUG) {
			Crashlytics.logException(error);
		}
	}

	protected <T> Observable<T> bind(Observable<T> observable) {
		return bindFragment(this, observable);
	}

	// Activity calls - shortcuts to methods on Activity or BaseActivity

	public void openFragment(FullFragment fragment, boolean append) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity != null) {
			activity.openFragment(fragment, append);
		}
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
}
