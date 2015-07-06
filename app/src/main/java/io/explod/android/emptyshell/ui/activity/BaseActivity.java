package io.explod.android.emptyshell.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.explod.android.emptyshell.ui.dialog.HasDialogs;
import io.explod.android.emptyshell.ui.dialog.HasToast;
import rx.Observable;

import static rx.android.app.AppObservable.bindActivity;

public abstract class BaseActivity extends AppCompatActivity implements HasDialogs, HasToast {

	private ProgressDialog mProgressDialog;

	private AlertDialog mAlertDialog;

	@Override
	public void showAlertDialog(@StringRes int messageResId) {
		showAlertDialog(getResources().getText(messageResId));
	}

	@Override
	public void showAlertDialog(CharSequence message) {
		hideDialogs();
		if (mAlertDialog == null) {
			mAlertDialog = new AlertDialog.Builder(this).setPositiveButton(android.R.string.ok, null).create();
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
			mProgressDialog = new ProgressDialog(this);
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
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void toastLong(@StringRes int messageResId) {
		Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
	}

	protected <T> Observable<T> bind(Observable<T> observable) {
		return bindActivity(this, observable);
	}
}
