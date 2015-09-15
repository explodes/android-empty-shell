package io.explod.android.emptyshell.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.RefWatcher;

import java.io.Serializable;
import java.util.ArrayList;

import icepick.State;
import io.explod.android.emptyshell.App;
import io.explod.android.emptyshell.BuildConfig;
import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.ui.dialog.HasDialogs;
import io.explod.android.emptyshell.ui.dialog.HasToast;
import io.explod.android.emptyshell.ui.fragment.FragmentState;
import io.explod.android.emptyshell.ui.fragment.FullFragment;
import rx.Observable;

import static android.text.TextUtils.isEmpty;
import static rx.android.app.AppObservable.bindActivity;

public abstract class BaseActivity extends AppCompatActivity implements HasDialogs, HasToast {

	private static final String TAG = BaseActivity.class.getSimpleName();

	private static class FragmentStackItem implements Serializable {

		@NonNull
		String name;

		@NonNull
		FragmentState state;

		FragmentStackItem(@NonNull FullFragment fragment) {
			name = fragment.getClass().getName();
			state = fragment.getFragmentState();
		}
	}

	private ProgressDialog mProgressDialog;

	private AlertDialog mAlertDialog;

	@State
	ArrayList<FragmentStackItem> mFragmentStackItems;

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

	public void openFragment(@NonNull FullFragment fragment, boolean appendFragment) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "openFragment " + appendFragment + ": " + fragment.getClass().getName());
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		// if this is going to be the only fragment, clear the title stack
		if (!appendFragment) {
			mFragmentStackItems.clear();
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		FragmentStackItem stackItem = new FragmentStackItem(fragment);
		stackItem.state.setTo(this);
		mFragmentStackItems.add(stackItem);

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.container, fragment, stackItem.state.getTag());
		if (appendFragment) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		popFragmentState();
		super.onBackPressed();
	}

	@Nullable
	public String getCurrentFragmentName() {
		if (mFragmentStackItems == null || mFragmentStackItems.isEmpty()) {
			return null;
		}
		int N = mFragmentStackItems.size();
		FragmentStackItem item = mFragmentStackItems.get(N - 1);
		return item.name;
	}

	public boolean isCurrentFragment(Class<? extends FullFragment> fragmentClass) {
		String currentFragmentName = getCurrentFragmentName();
		return !isEmpty(currentFragmentName) && currentFragmentName.equals(fragmentClass.getName());
	}

	protected boolean popFragmentState() {
		int size = mFragmentStackItems.size();
		if (size >= 2) { // if there is even a title to pop AND a title to use
			mFragmentStackItems.remove(size - 1); // last item

			// change the title to the last non-zero title
			FragmentStackItem stackItem = mFragmentStackItems.get(size - 2);
			// new last item
			stackItem.state.setTo(this);
			return true;
		}
		return false;
	}

	protected void logFragmentStack(String message) {
		Log.d(TAG, "fragment stack: " + message);

		if (mFragmentStackItems == null) {
			Log.d(TAG, " - empty");
			return;
		}

		for (int index = mFragmentStackItems.size() - 1; index >= 0; index--) {
			FragmentStackItem item = mFragmentStackItems.get(index);
			Log.d(TAG, " - " + item.name + " :: " + item.state);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title == null ? "" : title);
		}
	}

	public void setSubtitle(CharSequence subtitle) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setSubtitle(subtitle == null ? "" : subtitle);
		}
	}

	public void setSubtitle(@StringRes int subtitleRes) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			if (subtitleRes == 0) {
				actionBar.setSubtitle("");
			} else {
				actionBar.setSubtitle(subtitleRes);
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// restore top state
		// requires
		//     android:configChanges="orientation|screenSize"
		// in activity manifest
		int size = mFragmentStackItems == null ? 0 : mFragmentStackItems.size();
		if (size == 0) {
			return;
		}
		mFragmentStackItems.get(size - 1).state.setTo(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RefWatcher refWatcher = App.getRefWatcher();
		refWatcher.watch(this);
	}
}
