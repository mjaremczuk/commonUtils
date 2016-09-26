package pl.revo.helperutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;

public class ShadowImageView extends ImageView {

	// Default property values
	private static final boolean SHADOW_ENABLED = true;
	private static final float SHADOW_RADIUS = 4f;
	private static final float SHADOW_DX = 2f;
	private static final float SHADOW_DY = 2f;
	private static final int SHADOW_COLOR = Color.BLACK;

	// Shadow properties
	private boolean shadowEnabled;
	private float shadowRadius;
	private float shadowDx;
	private float shadowDy;
	private int shadowColor;


	// Objects used for the actual drawing
//	private BitmapShader shader;
	private Bitmap image;
	private Paint paint;

	public ShadowImageView(Context context) {
		this(context, null);
	}

	public ShadowImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShadowImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	void init(AttributeSet attrs) {
		paint = new Paint();
//		paint.setAntiAlias(true);
		// Enable software rendering on HoneyComb and up. (needed for shadow)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setLayerType(LAYER_TYPE_SOFTWARE, null);
		}
		if (attrs != null) {
			// Load the styled attributes and set their properties
			TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowImageView);
			shadowEnabled = attributes.getBoolean(R.styleable.ShadowImageView_dropShadow, SHADOW_ENABLED);
			shadowColor = attributes.getColor(R.styleable.ShadowImageView_shadowColor, SHADOW_COLOR);
			shadowRadius = attributes.getDimension(R.styleable.ShadowImageView_shadowRadius, SHADOW_RADIUS);
			shadowDx = attributes.getDimension(R.styleable.ShadowImageView_shadowDx, SHADOW_DX);
			shadowDy = attributes.getDimension(R.styleable.ShadowImageView_shadowDy, SHADOW_DY);
			attributes.recycle();
		}
		if (shadowEnabled) {
			paint.setDither(true);
			paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Don't draw anything without an image
		if (image == null)
			return;
		// Nothing to draw (Empty bounds)
		if (image.getHeight() == 0 || image.getWidth() == 0)
			return;
		canvas.drawBitmap(image,0f,0f,paint);
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		// Extract a Bitmap out of the drawable & set it as the main shader
		image = drawableToBitmap(getDrawable());
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		// Extract a Bitmap out of the drawable & set it as the main shader
		image = drawableToBitmap(getDrawable());
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		// Extract a Bitmap out of the drawable & set it as the main shader
		image = drawableToBitmap(getDrawable());
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		// Extract a Bitmap out of the drawable & set it as the main shader
		image = bm;
	}

	/**
	 * Convert a drawable object into a Bitmap.
	 *
	 * @param drawable Drawable to extract a Bitmap from.
	 *
	 * @return A Bitmap created from the drawable parameter.
	 */
	public Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {  // Don't do anything without a proper drawable
			return null;
		}
		else if (drawable instanceof BitmapDrawable) {  // Use the getBitmap() method instead if BitmapDrawable
			Log.i(TAG, "Bitmap drawable!");
			return ((BitmapDrawable) drawable).getBitmap();
		}
		else {
			return null;
		}
//		int intrinsicWidth = drawable.getIntrinsicWidth();
//		int intrinsicHeight = drawable.getIntrinsicHeight();
//		if (!(intrinsicWidth > 0 && intrinsicHeight > 0)) {
//			return null;
//		}
//		try {
//			// Create Bitmap object out of the drawable
//			Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ALPHA_8);
//			Canvas canvas = new Canvas(bitmap);
//			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//			drawable.draw(canvas);
//			return bitmap;
//		} catch (OutOfMemoryError e) {
//			e.printStackTrace();
//			return null;
//		}
	}
}
