package pl.revo.helperutils.presenter;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Presenter  for handling fragment in activity
 */
public class NavigationPresenter {

	private static final int PRESS_COUNT_TO_SHOW_NOTIFICATION = 1;
	public static boolean isAPI_19 = (VERSION.SDK_INT >= VERSION_CODES.KITKAT);

	private static final int MIN_TIME_TO_RESET_CLICKS = 2000;
	FragmentManager fragmentManager;
	Fragment currentFragment;
	int fragmentContainerId, pressCounts;
	Handler pressesHandler;
	NavigationDataView dataView;

	public NavigationPresenter(NavigationDataView view, FragmentManager manager, int fragmentContainerId) {
		this.dataView = view;
		this.fragmentManager = manager;
		this.fragmentContainerId = fragmentContainerId;
		pressCounts = 0;
	}

	/**
	 * Check if fragment manager is null or destroyed
	 *
	 * @return true if fragment manager is usable false otherwise
	 */
	private boolean isFragmentManagerExists() {
		return (fragmentManager != null && !fragmentManager.isDestroyed());
	}

	/**
	 * Bind presenter with fragment with {@param view} since now presenter can communicate with view
	 *
	 * @param view interface which allow communication between presenter and view
	 * @param manager fragment manager
	 * @param fragmentContainerId id of view where fragments gonna be replaced
	 */
	public void bindView(NavigationDataView view, FragmentManager manager, int fragmentContainerId) {
		this.dataView = view;
		this.fragmentManager = manager;
		this.fragmentContainerId = fragmentContainerId;
		pressCounts = 0;
	}

	private <T extends Fragment> Fragment addFragmentToBackStackPrivate(T fragment, boolean allowAddSameClass) {
		if (!allowAddSameClass) {
			if (currentFragment == null || fragment.getClass() != currentFragment.getClass()) {
				return addFragmentToBackStackInternal(fragment, false);
			}
			else {
				return null;
			}
		}
		else {
			return addFragmentToBackStackInternal(fragment, true);
		}
	}

	private <T extends Fragment> Fragment addFragmentToBackStackInternal(T fragment, boolean allowAddSameClass) {
		if (isFragmentManagerExists()) {
			fragmentTransaction(fragment);
			currentFragment = fragment;
			return fragment;
		}
		else {
			fragmentManager = dataView.presenterFragmentManager();
			return addFragmentToBackStackPrivate(fragment, allowAddSameClass);
		}
	}

	private <T extends Fragment> void fragmentTransaction(T fragment) {
		String key = "fragment" + fragmentManager.getBackStackEntryCount();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction
				.replace(fragmentContainerId, fragment, key)
				.addToBackStack(key);
		if (isAPI_19) {
			transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		transaction.commitAllowingStateLoss();
	}

	/**
	 * Adds new fragment to back stack of fragment manager object
	 *
	 * @param fragment fragment to be added to back stack
	 * @param <T> type of fragment
	 *
	 * @return Fragment which is on top of back stack
	 */
	public <T extends Fragment> Fragment addFragmentToBackStack(T fragment) {
		return addFragmentToBackStackPrivate(fragment, false);
	}

	public <T extends Fragment> Fragment addFragmentToBackStack(T fragment, boolean addSameClassFragment) {
		return addFragmentToBackStackPrivate(fragment, addSameClassFragment);
	}

	public <T extends Fragment> void popStackToFragmentImmediate(T fragment) {
		if (isFragmentManagerExists()) {
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			addFragmentToBackStack(fragment);
		}
		else {
			popStackToFragmentImmediate(fragment);
		}
	}

	/**
	 * Override view {@link Activity#onBackPressed()} to handle proper action with fragments
	 *
	 * @return Fragment which is actually on top of back stack
	 */
	public Fragment onBackPressed() {
		if (isFragmentManagerExists()) {
			return popFragment();
		}
		else {
			fragmentManager = dataView.presenterFragmentManager();
			return onBackPressed();
		}
	}

	private Fragment popFragment() {
		if (fragmentManager.getBackStackEntryCount() > 1) {
			return tryPopFragmentFromStack();
		}
		else {
			onPopLastFragment();
			return null;
		}
	}

	private Fragment tryPopFragmentFromStack() {
		try {
			fragmentManager.popBackStackImmediate();
			String tag = "fragment" + (fragmentManager.getBackStackEntryCount() - 1);
			Fragment poppedFragment = fragmentManager.findFragmentByTag(tag);
			currentFragment = poppedFragment;
			return poppedFragment;
		}
		catch (IllegalStateException ex) {
			return null;
		}
	}

	/**
	 * Add multiple fragments for back stack
	 *
	 * @param fragments array of fragments
	 * @param <T> type of fragments acceptable by method
	 */
	public <T extends Fragment> void createStackBackList(T... fragments) {
		for (T fragment : fragments) {
			addFragmentToBackStack(fragment);
		}
	}

	/**
	 * Finds fragment by its id
	 *
	 * @param id id of fragment
	 *
	 * @return Fragment found or null
	 */
	public Fragment findFragmentById(int id) {
		if (isFragmentManagerExists()) {
			return fragmentManager.findFragmentById(id);
		}
		else {
			fragmentManager = dataView.presenterFragmentManager();
			return findFragmentById(id);
		}
	}

	public Fragment getCurrentFragment() {
		return currentFragment;
	}

	/**
	 * Handle double click before user close application.
	 * To avoid mistaken single click before close application
	 * we gonna ask him for double click before close application
	 */
	private void onPopLastFragment() {
		pressCounts += 1;
		postBackPressAction();
		if (pressCounts == PRESS_COUNT_TO_SHOW_NOTIFICATION) {
			dataView.userAttemptToQuitApp();
		}
		else {
			dataView.closeApplication();
		}
	}

	private void postBackPressAction() {
		if (pressesHandler == null) {
			pressesHandler = new Handler();
			pressesHandler.postDelayed(() -> {
				pressCounts = 0;
				pressesHandler = null;
			}, MIN_TIME_TO_RESET_CLICKS);
		}
	}
}
