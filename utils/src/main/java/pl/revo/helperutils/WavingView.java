package pl.revo.helperutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WavingView extends View {
	// TODO: 24.10.2016 organize, if should do by waves count or wave offsets and then start creating waves appropriate, for each wave add few iteration when only alpha changes

	public static final int DEFAULT_WAVE_DURATION = 500;
	public int FRAME = 10;
	private int WAVE_COUNT = 5;
	public int elapsedDuration = 0;
	public int waveOffset = 100;
	int waveDuration = 500;//duration in milis
	Paint wavePaint;
	Paint centerPaint;
	int startSize = 10;
	int maxSize = 400;
	int drawableRes;
	List<Wave> waves;
	Handler waveHandler;
	boolean isRunning = false;
	WeakReference<Bitmap> bitmap;
	Rect imageRect, destRect;

	public WavingView(Context context) {
		this(context, null);
	}

	public WavingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WavingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	void init(AttributeSet attrs) {
		setWillNotDraw(false);
		if (attrs != null) {
			TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.WavingView);
			startSize = attributes.getDimensionPixelSize(R.styleable.WavingView_startSize, FRAME);
			waveDuration = attributes.getInteger(R.styleable.WavingView_waveDuration, DEFAULT_WAVE_DURATION);
			WAVE_COUNT = attributes.getInteger(R.styleable.WavingView_waveCount, WAVE_COUNT);
			drawableRes = attributes.getResourceId(R.styleable.WavingView_centerImage, 0);
			attributes.recycle();
		}
		if (drawableRes != 0) {
			bitmap = new WeakReference<>(BitmapFactory.decodeResource(getResources(), drawableRes));
			imageRect = new Rect(0, 0, bitmap.get().getWidth(), bitmap.get().getHeight());
		}
		waveHandler = new Handler();
		wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		wavePaint.setStyle(Style.FILL);
		wavePaint.setColor(Color.BLUE);
		wavePaint.setAlpha(0);
		centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		centerPaint.setStyle(Style.FILL_AND_STROKE);
		centerPaint.setColor(Color.BLACK);
		centerPaint.setMaskFilter(new BlurMaskFilter(8, Blur.NORMAL));
		waves = new ArrayList<>();
		ViewTreeObserver vto = getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
					WavingView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				else {
					WavingView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
				maxSize = getMeasuredWidth() / 2;
				if (bitmap != null) {
					destRect = new Rect(maxSize - startSize, maxSize - startSize, maxSize + startSize, maxSize + startSize);
				}
				int iterations = waveDuration / FRAME;
				waveOffset = iterations / WAVE_COUNT * FRAME;
				for (int i = 0; i < WAVE_COUNT; i++) {
					waves.add(buildWave(iterations));
				}
			}
		});
	}

	public Wave buildWave(int iterations) {
		Wave wave = new WaveItem();
		wave.setPaint(wavePaint);
		List<WaveFrameItem> items = new ArrayList<>();
		int maxWidth = 0;
		int alpha = 255;
		int j = 0;
		float alphaIter = (float) 255 / ((waveDuration) / (float) FRAME);
		int wIter = (maxSize - startSize) / iterations;
		for (int i = 0; i < waveDuration; i += FRAME) {
			int alphaDec = (int) (alphaIter * j);
			alpha = 255 - (alphaDec < 255 ? alphaDec : 255);
			maxWidth = wIter * j + startSize;
			j++;
			WaveFrameItem item = new WaveFrameItem(alpha, maxWidth);
			items.add(item);
		}
		wave.setItems(items);
		return wave;
	}

	public void startWaving() {
		for (Wave wave : waves) {
			wave.setPaused(false);
		}
		waveHandler.postDelayed(redrawRunnable, FRAME);
		isRunning = true;
	}

	public void stopWaving() {
		for (Wave wave : waves) {
			wave.setPaused(true);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int x = getWidth();
		int y = getHeight();
		if (x > 0 && y > 0) {
			for (int i = 0; i < waves.size(); i++) {
				if (elapsedDuration >= i * waveOffset) {
					Wave item = waves.get(i);
					item.draw(canvas, x / 2, y / 2);
				}
			}
			canvas.drawCircle(x / 2, y / 2, startSize, centerPaint);
			if (bitmap != null && bitmap.get() != null) {
				destRect.left = (x / 2 - startSize);
				destRect.top = (y / 2 - startSize);
				destRect.right = (x / 2 + startSize);
				destRect.bottom = (y / 2 + startSize);
				canvas.drawBitmap(bitmap.get(), imageRect, destRect, centerPaint);
			}
		}
	}

	public Runnable redrawRunnable = new Runnable() {
		@Override
		public void run() {
			elapsedDuration += FRAME;
			invalidate();
//			for (int i = 0; i < waves.size(); i++) {
//				if (elapsedDuration >= i * waveOffset) {
//					WaveItem item = waves.get(i);
//					item.iteration = item.iteration + 1;
//				}
//			}

			boolean finished = true;
			for (int i = 0; i < waves.size(); i++) {
				if (!waves.get(i).animationCompleted()) {
					finished = false;
					break;
				}
			}
			if (!finished) {
				waveHandler.postDelayed(redrawRunnable, FRAME);
			}
			else {
				waveHandler.removeCallbacks(redrawRunnable);
				elapsedDuration = 0;
				for (Wave wave : waves) {
					wave.setIteration(0);
					wave.setAnimationCompleted(false);
				}
				isRunning = false;
//				invalidate();
			}
		}
	};

	public static class WaveItem implements Wave {
		List<WaveFrameItem> items;
		Paint wavePaint;
		int iteration = 0;
		boolean pause, animationCompleted;

		WaveItem() {
			pause = true;
			animationCompleted = false;
		}

		int size() {
			return items.get(iteration % items.size()).size;
		}

		Paint paint() {
			wavePaint.setAlpha(items.get(iteration % items.size()).alpha);
			return wavePaint;
		}

		@Override
		public void draw(Canvas canvas, int x, int y) {
			if (pause) {
				if (iteration % items.size() != 0) {
					animationCompleted = false;
					iteration++;
					canvas.drawCircle(x, y, size(), paint());
				}
				else {
					animationCompleted = true;
				}
			}
			else {
				animationCompleted = false;
				iteration++;
				canvas.drawCircle(x, y, size(), paint());
			}
		}

		@Override
		public boolean animationCompleted() {
			return animationCompleted;
		}

		@Override
		public int iteration() {
			return iteration;
		}

		@Override
		public boolean pasued() {
			return pause;
		}

		@Override
		public void setPaused(boolean paused) {
			this.pause = paused;
		}

		@Override
		public void setAnimationCompleted(boolean completed) {
			this.animationCompleted = completed;
		}

		@Override
		public void setIteration(int iteration) {
			this.iteration = iteration;
		}

		@Override
		public void setPaint(Paint paint) {
			this.wavePaint = paint;
		}

		@Override
		public void setItems(List<WaveFrameItem> items) {
			this.items = items;
		}
	}

	public static class WaveFrameItem {

		int alpha;
		int size;

		WaveFrameItem(int alpha, int size) {
			this.alpha = alpha;
			this.size = size;
		}
	}

	public interface Wave {
		void draw(Canvas canvas, int x, int y);

		boolean animationCompleted();

		int iteration();

		boolean pasued();

		void setPaused(boolean paused);

		void setAnimationCompleted(boolean completed);

		void setIteration(int iteration);

		void setPaint(Paint paint);

		void setItems(List<WaveFrameItem> items);

	}
}
