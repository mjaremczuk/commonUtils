package pl.revo.helperutils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.annotation.ColorInt;
import pl.revo.helperutils.DrawableUtils.Builder.OrientationStep;
import pl.revo.helperutils.DrawableUtils.Builder.StepBuilder;

public class DrawableUtils {

	/**
	 * Create drawable shape with stroke with desired solid color and stroke color and width
	 *
	 * @param bottomColor Drawable solid color
	 * @param colorStroke Drawable stroke color
	 * @param strokeSize Drawable stroke width
	 *
	 * @return Drawable
	 */
	public static Drawable generateShapeWithStroke(@ColorInt int bottomColor, @ColorInt int topColor, @ColorInt int colorStroke,
			int strokeSize) {
		GradientDrawable drawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{bottomColor, topColor});
		drawable.setStroke(strokeSize, colorStroke);
		return drawable;
	}

	public static Drawable generateShapeWithStroke(Orientation orientation,@ColorInt int bottomColor, @ColorInt int topColor, @ColorInt int colorStroke,
			int strokeSize,float cornerRadius) {
		// values are require to be in pixels
		GradientDrawable drawable = new GradientDrawable(orientation, new int[]{bottomColor, topColor});
		drawable.setStroke(strokeSize, colorStroke);
		drawable.setCornerRadius(cornerRadius);
		return drawable;
	}

	public static OrientationStep builder() {
		return new StepBuilder();
	}

	public static class Builder {

		Orientation orientation;
		int bgColorFirst, bgColorSecond;
		int strokeColor;
		int strokeWidth;
		float cornerRadius;

		public Builder() {
		}

		public Drawable draw() {
			return generateShapeWithStroke(orientation,bgColorFirst,bgColorSecond,strokeColor,strokeWidth,cornerRadius);
		}


		public static class StepBuilder
				implements OrientationStep, ColorStep, StrokeStep, DrawStep, ColorTwoStep, StrokeColorStep,CornerRadiusStep {

			Orientation orientation;
			int bgColorFirst, bgColorSecond;
			int strokeColor;
			int strokeWidth;
			float cornerRadius;

			@Override
			public ColorTwoStep color(@ColorInt int color) {
				this.bgColorFirst = color;
				return this;
			}

			@Override
			public CornerRadiusStep cornerRadius(float radius) {
				cornerRadius = radius;
				return this;
			}

			@Override
			public StrokeStep strokeColor(@ColorInt int color) {
				this.strokeColor = color;
				return this;
			}

			@Override
			public StrokeColorStep secondColor(@ColorInt int color) {
				this.bgColorSecond = color;
				return this;
			}

			@Override
			public Drawable draw() {
				Builder builder = new Builder();
				builder.orientation = orientation;
				builder.bgColorFirst = bgColorFirst;
				builder.bgColorSecond = bgColorSecond;
				builder.strokeColor = strokeColor;
				builder.strokeWidth = strokeWidth;
				builder.cornerRadius = cornerRadius;
				return builder.draw();
			}

			@Override
			public ColorStep orientation(Orientation orientation) {
				this.orientation = orientation;
				return this;
			}

			@Override
			public DrawStep strokeWidth(int width) {
				strokeWidth = width;
				return this;
			}
		}

		public interface OrientationStep {

			ColorStep orientation(Orientation orientation);
		}

		public interface ColorStep {

			ColorTwoStep color(@ColorInt int color);

			CornerRadiusStep cornerRadius(float radius);

			Drawable draw();
		}

		public interface ColorTwoStep {

			StrokeColorStep secondColor(@ColorInt int color);

			StrokeStep strokeColor(@ColorInt int color);

			Drawable draw();
		}

		public interface StrokeColorStep {
			StrokeStep strokeColor(@ColorInt int color);

			CornerRadiusStep cornerRadius(float radius);

			Drawable draw();
		}

		public interface StrokeStep {

			DrawStep strokeWidth(int width);
		}

		public interface CornerRadiusStep {

			Drawable draw();
		}

		public interface DrawStep {

			CornerRadiusStep cornerRadius(float radius);

			Drawable draw();
		}

	}
}
