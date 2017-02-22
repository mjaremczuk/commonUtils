package pl.revo.helperutils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.TintableBackgroundView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing quick tint of views and drawables
 */
public class TintUtils {

	public enum StateType {

		/**
		 * States: DEFAULT;
		 */
		DEFAULT(STATE_DEFAULT),

		/**
		 * States: PRESSED,DEFAULT
		 */
		PRESSED_DEFAULT(STATE_PRESS_DEFAULT),
		/**
		 * States: UNAVAILABLE, PRESSED, DEFAULT
		 */
		DPD(STATE_3),
		/**
		 * States: CHECKED, UNCHECKED
		 */
		CHECKED_UNCHECKED(STATE_CHECKED_UNCHECKED),

		/**
		 * States: SELECTED, PRESSED, DEFAULT
		 */
		SPD(STATE_3_SELECTED);

		public int[][] states;

		StateType(int[][] stateArray) {
			this.states = stateArray;
		}
	}

	public static final int[][] STATE_DEFAULT = {{0}};
	public static final int[][] STATE_PRESS_DEFAULT = {{android.R.attr.state_pressed}, {0}};
	public static final int[][] STATE_3 = {{-android.R.attr.state_enabled}, {android.R.attr.state_pressed}, {0}};
	public static final int[][] STATE_3_SELECTED = {{android.R.attr.state_selected}, {android.R.attr.state_pressed}, {0}};
	public static final int[][] STATE_CHECKED_UNCHECKED = {{android.R.attr.state_checked}, {-android.R.attr.state_checked}};

	/**
	 * Tint any view that implements {@link TintableBackgroundView}
	 *
	 * @param view view to tint
	 * @param states states to which colors will be applied
	 * @param color array of resource colors to apply
	 * @param <T> view must extends View and implements interface
	 */
	private static <T extends View & TintableBackgroundView> void tint(T view, int[][] states, @ColorInt int... color) {
		view.setSupportBackgroundTintList(new ColorStateList(states, color));
	}

	private static int[] toInts(Integer... integers) {
		int[] result = new int[integers.length];
		for (int i = 0; i < integers.length; i++) {
			result[i] = integers[i];
		}
		return result;
	}

	/**
	 * Tint any view that implements {@link TintableBackgroundView}
	 *
	 * @param view view to tint
	 * @param states states to which colors will be applied
	 * @param color array of resource colors to apply
	 * @param <T> view must extends view and implements interface
	 */
	private static <T extends View & TintableBackgroundView> void tintWithResColor(T view, int[][] states,
			@ColorRes Integer... color) {
		tint(view, states, resColorsToIntColor(view.getContext(), color));
	}


	/**
	 * Add tint list to desired drawable
	 *
	 * @param drawable drawable to apply tint list
	 * @param states list of states to which apply colors
	 * @param colors list of colors for states
	 *
	 * @return return drawable with applied tint list
	 */
	private static Drawable tintDrawable(Drawable drawable, int[][] states, @ColorInt int... colors) {
		Drawable wrapDrawable = DrawableCompat.wrap(drawable);
		// to apply only color overlay for drawable set tint mode to SRC_ATOP
		DrawableCompat.setTintMode(wrapDrawable, Mode.SRC_ATOP);
		DrawableCompat.setTintList(wrapDrawable, new ColorStateList(states, colors));
		return wrapDrawable;
	}

	/**
	 * Add tint list to desired drawable resource
	 *
	 * @param context context for retrieving drawable
	 * @param drawable drawable resource int
	 * @param states state type
	 * @param colors array of color ints
	 *
	 * @return drawable with tint applied
	 */
	private static Drawable tintDrawable(Context context, @DrawableRes int drawable, int[][] states, @ColorInt int... colors) {
		Drawable drawableObj = ActivityCompat.getDrawable(context, drawable);
		return tintDrawable(drawableObj, states, colors);
	}


	/**
	 * Add tint list to desired drawable resource
	 *
	 * @param context context for retrieving drawable
	 * @param drawable drawable resource int
	 * @param states state type
	 * @param colors array of color ints
	 *
	 * @return drawable with tint applied
	 */
	private static Drawable tintDrawableWithResourceColor(Context context, @DrawableRes int drawable, int[][] states,
			@ColorRes Integer... colors) {
		return tintDrawable(context, drawable, states, resColorsToIntColor(context, colors));
	}

	/**
	 * Add tint list to desired drawable using resource colors
	 *
	 * @param context context for retrieving color integers
	 * @param drawable drawable to be tinted
	 * @param states states type to apply to drawable
	 * @param colors array of colors to apply
	 *
	 * @return tinted drawable
	 */
	private static Drawable tintDrawableWithResColor(Context context, Drawable drawable, int[][] states,
			@ColorRes Integer... colors) {
		return tintDrawable(drawable, states, resColorsToIntColor(context, colors));
	}

	/**
	 * Convert resource color array to int color array
	 *
	 * @param context context for retrieving color int
	 * @param resColors array of resource int colors
	 *
	 * @return array of int colors
	 */
	private static int[] resColorsToIntColor(Context context, @ColorRes Integer... resColors) {
		int[] colors = new int[resColors.length];
		for (int i = 0; i < resColors.length; i++) {
			colors[i] = ActivityCompat.getColor(context, resColors[i]);
		}
		return colors;
	}

	public static InitialStep stepBuilder() {
		return new Steps();
	}

	public static InitialContextStep builder(Context context) {
		return new Steps(context);
	}

	public static class Builder {

		List<Integer> colorResList;
		int[][] type;
		boolean resourceColor;
		Context context;

		public Builder() {
			colorResList = new ArrayList<>();
			resourceColor = false;
		}

		public <T extends View & TintableBackgroundView> void tint(T view) {
			if (resourceColor) {
				TintUtils.tintWithResColor(view, type, colorResList.toArray(new Integer[colorResList.size()]));
			}
			else {
				TintUtils.tint(view, type, toInts(colorResList.toArray(new Integer[colorResList.size()])));
			}
		}

		public Drawable tint(Drawable drawable) {
			if (resourceColor) {
				return tintWithResourceColors(drawable);
			}
			else {
				return tintWithColors(drawable);
			}
		}

		public Drawable tint(int drawableRes) {
			if (context != null) {
				if (resourceColor) {
					return TintUtils.tintDrawableWithResourceColor(context, drawableRes, type,
							colorResList.toArray(new Integer[colorResList.size()]));
				}
				else {
					return TintUtils.tintDrawable(context, drawableRes, type,
							toInts(colorResList.toArray(new Integer[colorResList.size()])));
				}
			}
			else {
				throw new NullPointerException("Context not set up for tint drawable resource");
			}
		}

		private Drawable tintWithResourceColors(Drawable drawable) {
			if (context != null) {
				return TintUtils.tintDrawableWithResColor(context, drawable, type,
						colorResList.toArray(new Integer[colorResList.size()]));
			}
			else {
				throw new NullPointerException(
						"Context not set up for tint drawable with color resources, try using tint with color values instead");
			}
		}

		private Drawable tintWithColors(Drawable drawable) {
			return TintUtils.tintDrawable(drawable, type, toInts(colorResList.toArray(new Integer[colorResList.size()])));
		}
	}

//	Step build pattern classes and interfaces

	public static class Steps implements ColorStep, TypeStep, ColorResStep, TintStep, InitialStep, InitialContextStep {

		List<Integer> colorResList;
		int[][] type;
		boolean resourceColor;
		Context context;

		public Steps() {
			colorResList = new ArrayList<>();
			resourceColor = false;
		}

		public Steps(Context context) {
			this();
			this.context = context;
		}

		@Override
		public <T extends View & TintableBackgroundView> void tint(T view) {
			Builder builder = new Builder();
			builder.type = type;
			builder.resourceColor = resourceColor;
			builder.colorResList = colorResList;
			builder.tint(view);
		}

		@Override
		public Drawable tint(Drawable drawable) {
			Builder builder = new Builder();
			builder.type = type;
			builder.resourceColor = resourceColor;
			builder.colorResList = colorResList;
			builder.context = context;
			return builder.tint(drawable);
		}

		@Override
		public Drawable tint(int drawableRes) {
			Builder builder = new Builder();
			builder.type = type;
			builder.resourceColor = resourceColor;
			builder.colorResList = colorResList;
			builder.context = context;
			return builder.tint(drawableRes);
		}

		@Override
		public ColorResStep withResColor(@ColorRes int colorRes) {
			resourceColor = true;
			colorResList.add(colorRes);
			return this;
		}

		@Override
		public ColorStep withColor(@ColorInt int color) {
			resourceColor = false;
			colorResList.add(color);
			return this;
		}

		@Override
		public TypeStep withType(StateType type) {
			this.type = type.states;
			return this;
		}

		@Override
		public TypeStep withType(int[][] type) {
			this.type = type;
			return this;
		}
	}

	public interface ColorStep {
		ColorStep withColor(@ColorInt int color);

		<T extends View & TintableBackgroundView> void tint(T view);

		Drawable tint(Drawable drawable);

		Drawable tint(int drawableRes);
	}

	public interface ColorResStep {
		ColorResStep withResColor(@ColorRes int colorRes);

		<T extends View & TintableBackgroundView> void tint(T view);

		Drawable tint(Drawable drawable);

		Drawable tint(int drawableRes);
	}

	public interface TypeStep {
		ColorStep withColor(@ColorInt int color);

		ColorResStep withResColor(@ColorRes int colorRes);
	}


	public interface InitialStep {
		TypeStep withType(StateType type);
	}

	public interface InitialContextStep {
		TypeStep withType(StateType type);
		TypeStep withType(int[][] type);
	}


	public interface TintStep {
		<T extends View & TintableBackgroundView> void tint(T view);

		Drawable tint(Drawable drawable);

		Drawable tint(int drawableRes);
	}
}
