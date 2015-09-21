package io.explod.android.emptyshell.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.explod.android.emptyshell.R;

import static io.explod.android.emptyshell.App.getApp;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends FullFragment {

	@NonNull
	public static FullFragment newInstance() {
		return new MainFragment();
	}

	TitleFragmentState mFragmentState = new TitleFragmentState(null, R.string.app_name, 0);

	public MainFragment() {
		getApp().getObjectGraph().inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@NonNull
	@Override
	public FragmentState getFragmentState() {
		return mFragmentState;
	}

}
