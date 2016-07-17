package pl.revo.helperutils.presenter;

import android.support.v4.app.FragmentManager;


public interface NavigationDataView {
	void closeApplication();

	FragmentManager presenterFragmentManager();

	void userAttemptToQuitApp();
}
