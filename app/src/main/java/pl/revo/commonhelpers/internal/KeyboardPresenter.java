package pl.revo.commonhelpers.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Button;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import pl.revo.commonhelpers.R;
import pl.revo.commonhelpers.internal.CustomKeyboard.SuggestionItem;
import pl.revo.helperutils.DrawableUtils;
import pl.revo.helperutils.TintUtils;
import pl.revo.helperutils.TintUtils.StateType;

import static android.R.color.transparent;

public class KeyboardPresenter {

	private final KeyboardDataView dataView;
	private int[] random = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private Drawable[] standardViewBgs;
	private Drawable[] possibleViewBgs;
	private List<SuggestionItem> suggestionList;
	private String text,countryCode;
	private Subject<String> textSubject;

	interface KeyboardDataView{

		void onCountryCodeSelected(int position);

		void onPossibleNumbersFound(List<Integer> positions, Drawable[] standardViewBgs, Drawable[] possibleViewBgs);
	}

	KeyboardPresenter(KeyboardDataView dataView){
		this.dataView = dataView;
	}

	 void initialize(Context context,List<Button> views, int color) {
		textSubject = ReplaySubject.create();
		textSubject.throttleWithTimeout(200, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(textObserver);
		standardViewBgs = new Drawable[views.size()];
		possibleViewBgs = new Drawable[views.size()];
		for (int i = 0; i < views.size(); i++) {
			Drawable drawable = DrawableUtils.builder().orientation(Orientation.BOTTOM_TOP)
					.color(color)
					.secondColor(color)
					.draw();
			Drawable drawable2 = DrawableUtils.builder().orientation(Orientation.BOTTOM_TOP)
					.color(color)
					.secondColor(color)
					.draw();
			Drawable selectedDrawable = TintUtils.builder(context)
					.withType(StateType.PRESSED_DEFAULT.states)
					.withResColor(R.color.cardview_shadow_start_color)
					.withResColor(R.color.colorPrimarySelected)
					.tint(drawable2);
			Drawable regularDrawable = TintUtils.builder(context)
					.withType(StateType.PRESSED_DEFAULT.states)
					.withResColor(R.color.cardview_shadow_start_color)
					.withResColor(transparent)
					.tint(drawable);
			views.get(i).setBackground(regularDrawable);
			views.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			standardViewBgs[i] = regularDrawable;
			possibleViewBgs[i] = selectedDrawable;
		}
		suggestionList = new ArrayList<>();
	}

	void publishText(String text) {
		textSubject.onNext(text);
	}

	public String getText(){
		return text;
	}

	void setCountryCode(String code) {
		this.countryCode = code;
	}

	String getCountryCode(){
		return countryCode;
	}

	private void markPossibleViews(CharSequence text) {
		List<Integer> possiblePositions = new ArrayList<>();
		if (!TextUtils.isEmpty(text)) {
			for (int i = 0; i < 4; i++) {
				possiblePositions.add(new Random().nextInt(random.length));
			}
		}
		dataView.onPossibleNumbersFound(possiblePositions,standardViewBgs,possibleViewBgs);
	}

	private Observer<String> textObserver = new Observer<String>() {
		@Override
		public void onSubscribe(Disposable d) {

		}

		@Override
		public void onNext(String value) {
			text = value;
			markPossibleViews(text);
			if (TextUtils.isEmpty(text)) {
				countryCode = null;
				dataView.onCountryCodeSelected(-1);
			}
		}

		@Override
		public void onError(Throwable e) {

		}

		@Override
		public void onComplete() {

		}
	};
}
