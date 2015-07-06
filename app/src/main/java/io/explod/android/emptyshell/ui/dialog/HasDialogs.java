package io.explod.android.emptyshell.ui.dialog;

public interface HasDialogs {

	public void showProgressDialog(int messageResId);

	public void showProgressDialog(CharSequence message);

	public void showAlertDialog(int messageResId);

	public void showAlertDialog(CharSequence message);

	public void hideDialogs();
}
