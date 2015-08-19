package io.explod.android.emptyshell.ui.dialog;

public interface HasDialogs {

    void showProgressDialog(int messageResId);

    void showProgressDialog(CharSequence message);

    void showAlertDialog(int messageResId);

    void showAlertDialog(CharSequence message);

    void hideDialogs();
}
