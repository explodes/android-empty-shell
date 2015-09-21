package io.explod.android.emptyshell.ui.activity;

import android.os.Bundle;

import io.explod.android.emptyshell.R;
import io.explod.android.emptyshell.ui.fragment.MainFragment;


public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			openFragment(MainFragment.newInstance(), false);
		}
	}

}
