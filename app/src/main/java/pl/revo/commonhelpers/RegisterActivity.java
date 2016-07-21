package pl.revo.commonhelpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.rey.material.widget.RadioButton;
import java.util.List;
import pl.revo.helperutils.TintUtils;
import pl.revo.helperutils.TintUtils.StateType;

public class RegisterActivity extends AppCompatActivity {

	@BindViews({R.id.team_red, R.id.team_yellow, R.id.team_blue})
	 public List<RadioButton> teamButtonViews;
	@BindView(R.id.register_and_login)
	AppCompatButton registerAndLoginView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.bind(this);
		int color = ActivityCompat.getColor(this,R.color.register_button);
		Drawable drawable = TintUtils.builder(this)
				.withType(StateType.PRESSED_DEFAULT)
				.withResColor(R.color.register_button_active)
				.withResColor(R.color.register_button)
				.tint(generateShapeWithStroke(color,color,color,0,pxToDp(this,19)));
		registerAndLoginView.setBackgroundDrawable(drawable);
	}

	@OnClick({R.id.team_red, R.id.team_yellow, R.id.team_blue})
	public void changeTeamAction(View view){
		switch (view.getId()){
			case R.id.team_red:
				checkSingleView(0);
				break;
			case R.id.team_yellow:
				checkSingleView(1);
				break;
			case R.id.team_blue:
				checkSingleView(2);
				break;
		}
	}

	private void checkSingleView(int index){
		for (int i = 0; i < teamButtonViews.size(); i++) {
			teamButtonViews.get(i).setChecked(i == index);
		}
	}

	public static Drawable generateShapeWithStroke(@ColorInt int bottomColor,@ColorInt int topColor, @ColorInt int colorStroke, int stokeSize,float radius) {
		GradientDrawable drawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{bottomColor, topColor});
		drawable.setStroke(stokeSize, colorStroke);
		drawable.setCornerRadius(radius);
//		drawable.setCornerRadii(new float[]{radius,radius,radius,radius,radius,radius,radius,radius});
		return drawable;
	}

	/**
	 * Convert emount of DP to PX
	 *
	 * @param dp number of dp
	 *
	 * @return px value equal to DP in device density
	 */
	public static int dpToPx(Context context, int dp) {
		return (int) (dp / context.getApplicationContext().getResources().getDisplayMetrics().density);
	}

	/**
	 * Convert amount of pixel to DP
	 *
	 * @param px number of pixels
	 *
	 * @return dp value equal to px in device density
	 */
	public static int pxToDp(Context context, int px) {
		return (int) (px * context.getApplicationContext().getResources().getDisplayMetrics().density);
	}
}
