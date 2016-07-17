package pl.revo.commonhelpers;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.revo.commonhelpers.ItemFragment.OnListFragmentInteractionListener;
import pl.revo.commonhelpers.dummy.DummyContent.DummyItem;
import pl.revo.helperutils.DrawableUtils;
import pl.revo.helperutils.TintUtils;
import pl.revo.helperutils.TintUtils.StateType;
import pl.revo.helperutils.presenter.NavigationDataView;
import pl.revo.helperutils.presenter.NavigationPresenter;

public class MainActivity extends AppCompatActivity implements NavigationDataView, OnListFragmentInteractionListener {

	NavigationPresenter presenter;
	@BindView(R.id.example_1)
	Button example1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		presenter = new NavigationPresenter(this, getSupportFragmentManager(), R.id.fragment_container);
		presenter.addFragmentToBackStack(new ItemFragment());
		Drawable drawable = DrawableUtils.generateShapeWithStroke(Color.BLUE,Color.RED, Color.BLACK, 2);
		example1.setBackground(
				TintUtils.builder(this)
						.withType(StateType.PRESSED_DEFAULT)
						.withColor(Color.argb(125,0,255,0))
						.withColor(Color.TRANSPARENT)
						.tint(drawable));
	}

	@Override
	public void onBackPressed() {
		presenter.onBackPressed();
	}

	@Override
	public void closeApplication() {
		finish();
	}

	@Override
	public FragmentManager presenterFragmentManager() {
		return getSupportFragmentManager();
	}

	@Override
	public void userAttemptToQuitApp() {
		Toast.makeText(this, "Click again to close", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onListFragmentInteraction(DummyItem item) {

	}
}
