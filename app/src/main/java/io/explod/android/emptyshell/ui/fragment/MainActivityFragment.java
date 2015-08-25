package io.explod.android.emptyshell.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.explod.android.emptyshell.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends FullFragment {

	TitleFragmentState mFragmentState = new TitleFragmentState(null, R.string.app_name, 0);

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@NonNull
	@Override
	public FragmentState getFragmentState() {
		return mFragmentState;
	}
}
