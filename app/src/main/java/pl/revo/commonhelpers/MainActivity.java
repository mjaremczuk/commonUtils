package pl.revo.commonhelpers;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.revo.commonhelpers.ItemFragment.OnListFragmentInteractionListener;
import pl.revo.commonhelpers.dummy.DummyContent.DummyItem;
import pl.revo.helperutils.DrawableUtils;
import pl.revo.helperutils.RadioController;
import pl.revo.helperutils.TintUtils;
import pl.revo.helperutils.TintUtils.StateType;
import pl.revo.helperutils.WavingView;
import pl.revo.helperutils.presenter.NavigationDataView;
import pl.revo.helperutils.presenter.NavigationPresenter;
import pl.revo.helperutils.utils.RxCustom;

public class MainActivity extends AppCompatActivity implements NavigationDataView, OnListFragmentInteractionListener {

	NavigationPresenter presenter;
	@BindView(R.id.example_1)
	Button example1;
	RadioController radioController;
	@BindView(R.id.edit_text)
	EditText editText;
	@BindView(R.id.waving_view)
	WavingView wavingView;
	@BindView(R.id.waving_view2)
	WavingView wavingView2;

	FirebaseRemoteConfig mFirebaseRemoteConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
//		StorageManager.init(this.getApplicationContext(),StorageType.EXTERNAL);
		mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//		EmojiManager.initialize(this);
//		radioController = new RadioController(this);
		presenter = new NavigationPresenter(this, getSupportFragmentManager(), R.id.fragment_container);
		presenter.addFragmentToBackStack(new ItemFragment());
//		Drawable drawable = DrawableUtils.generateShapeWithStroke(Color.BLUE,Color.RED, Color.BLACK, 2);
		// TODO: 19.07.2016 test drawable builder all variants
		Drawable drawable = DrawableUtils.builder().orientation(Orientation.BOTTOM_TOP)
				.color(Color.YELLOW)
				.secondColor(Color.BLUE)
				.strokeColor(Color.BLACK)
				.strokeWidth(0)
				.cornerRadius(40)
				.draw();
		example1.setBackground(
				TintUtils.builder(this)
						.withType(StateType.PRESSED_DEFAULT.states)
						.withColor(Color.argb(125, 0, 255, 0))
						.withColor(Color.TRANSPARENT)
						.tint(drawable));
		RxCustom.prepare(this).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(o -> Toast.makeText(this, "Returned; " + o, Toast.LENGTH_SHORT).show());
//		File file = getStorage().createFile("MyFile123.txt","asdkadjahsdk ");
//		File file2 = getStorage().createFile("test/test","MyFile123.txt","asdkadjahsdk ");
//		 getStorage().createFile("test/test","MyFile1234.txt","asdkadjahsdk ");
//		getStorage().createFile("test/test","MyFile12345.txt","asdkadjahsdk ");
//		getStorage().createFile("test/test","MyFile1236.txt","asdkadjahsdk ");
//		getStorage().createFolder("nowy/folder/do/testow");
//		List<File> files = StorageManager.getStorage().getFolderFiles("test/test");
//		Log.d("FILE", "onCreate: "+ files.size());
//		Log.d("Test", "onCreate: +"+obj.toString());

	}

	@OnClick(R.id.example_1)
	public void startSplash() {
//		radioController.startRadioService("http://icecast.omroep.nl/radio1-bb-mp3");
//		radioController.startRadioService("http://195.150.20.244:8000/rmf_classic");
//		radioController.startRadioService("http://195.150.20.9:8000/rmf_classic");
//		radioController.startRadioService("http://server-25.stream-server.nl:8400");
		System.out.println();
//		String text = EmojiParser.parseToAliases(editText.getText().toString());
//		String unicode = EmojiParser.parseToUnicode(text);
//		String custom = EmojiManager.parseToUnicode(editText.getText().toString());
//		String toAliases = EmojiManager.parseToAliases(editText.getText().toString());
//		Toast.makeText(this, toAliases + "\n" + custom, Toast.LENGTH_LONG).show();
		if (wavingView.isRunning()) {
			wavingView.stopWaving();
			wavingView2.stopWaving();
		}
		else {
			wavingView.startWaving();
			wavingView2.startWaving();
		}

//		startActivity(new Intent(this,LoginActivity.class));
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
