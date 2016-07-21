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
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.revo.helperutils.TintUtils;
import pl.revo.helperutils.TintUtils.StateType;

public class LoginActivity extends AppCompatActivity {

	@BindView(R.id.login_login_button)
	AppCompatButton loginView;
	@BindView(R.id.login_register_regular)
	AppCompatButton registerRegularView;
	@BindView(R.id.login_register_google)
	AppCompatButton loginWithGoogleView;
	@BindView(R.id.login_register_facebook)
	AppCompatButton loginWithFacebookView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		int color = ActivityCompat.getColor(this,R.color.register_button);
		Drawable drawable = TintUtils.builder(this)
				.withType(StateType.PRESSED_DEFAULT)
				.withResColor(R.color.register_button_active)
				.withResColor(R.color.register_button)
				.tint(generateShapeWithStroke(color,color,color,0,pxToDp(this,19)));
		loginView.setBackgroundDrawable(drawable);

		int transparent = ActivityCompat.getColor(this,android.R.color.transparent);
		Drawable drawableRegular = TintUtils.builder(this)
				.withType(StateType.PRESSED_DEFAULT)
				.withResColor(R.color.register_button_active)
				.withResColor(R.color.register_button)
				.tint(generateShapeWithStroke(transparent,transparent,color,pxToDp(this,1),pxToDp(this,19)));
		registerRegularView.setBackgroundDrawable(drawableRegular);
		int white = ActivityCompat.getColor(this,android.R.color.white);
		Drawable drawableGoogle = TintUtils.builder(this)
				.withType(StateType.PRESSED_DEFAULT)
				.withResColor(R.color.transparent_black)
				.withResColor(android.R.color.white)
				.tint(generateShapeWithStroke(white,white,color,0,pxToDp(this,19)));
		loginWithGoogleView.setBackgroundDrawable(drawableGoogle);

		int fb = ActivityCompat.getColor(this,R.color.colorPrimary);
		Drawable drawableFb = TintUtils.builder(this)
				.withType(StateType.PRESSED_DEFAULT)
				.withResColor(R.color.register_button_active)
				.withResColor(android.R.color.transparent)
				.tint(generateShapeWithStroke(fb,fb,color,0,pxToDp(this,19)));
		loginWithFacebookView.setBackgroundDrawable(drawableFb);

		Drawable gDrawable = TintUtils.builder(this)
				.withType(StateType.PRESSED_DEFAULT)
				.withResColor(R.color.transparent_black)
				.withResColor(android.R.color.transparent)
				.tint(R.drawable.google_login_logo);
		loginWithGoogleView.setCompoundDrawablesWithIntrinsicBounds(gDrawable,null,null,null);
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
