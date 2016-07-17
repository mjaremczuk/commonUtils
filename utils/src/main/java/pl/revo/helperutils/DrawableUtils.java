package pl.revo.helperutils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.annotation.ColorInt;

public class DrawableUtils {

	/**
	 * Create drawable shape with stroke with desired solid color and stroke color and width
	 *
	 * @param color Drawable solid color
	 * @param colorStroke Drawable stroke color
	 * @param stokeSize Drawable stroke width
	 *
	 * @return Drawable
	 */
	public static Drawable generateShapeWithStroke(@ColorInt int bottomColor,@ColorInt int topColor, @ColorInt int colorStroke, int stokeSize) {
		GradientDrawable drawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{bottomColor, topColor});
		drawable.setStroke(stokeSize, colorStroke);
		return drawable;
	}
}
