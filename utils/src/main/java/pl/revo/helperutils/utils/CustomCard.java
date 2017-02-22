package pl.revo.helperutils.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import pl.revo.helperutils.R;

public class CustomCard extends FrameLayout {

	public CustomCard(Context context) {
		this(context, null);
	}

	public CustomCard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
			init(attrs);
	}

	void init(AttributeSet attr){
		setBackgroundResource(R.drawable.card_background);
		if(attr != null){
			TypedArray attributes = getContext().obtainStyledAttributes(attr, R.styleable.CustomCard);
			int bgRes = attributes.getResourceId(R.styleable.CustomCard_cardBacgroundColor,0);
			float cornerRadius = attributes.getDimension(R.styleable.CustomCard_cardCornerRadius,0);
			float cornerTopLeft = attributes.getDimension(R.styleable.CustomCard_cardTopLeftCornerRadius,0);
			float cornerTopRight = attributes.getDimension(R.styleable.CustomCard_cardTopRigthCornerRadius,0);
			float cornerBottomLeft = attributes.getDimension(R.styleable.CustomCard_cardBottomLeftCornerRadius,0);
			float cornerBottomRight = attributes.getDimension(R.styleable.CustomCard_cardBottomRigthCornerRadius,0);
			attributes.recycle();
		}
	}
}
