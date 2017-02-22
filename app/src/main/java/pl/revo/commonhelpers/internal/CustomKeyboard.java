package pl.revo.commonhelpers.internal;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import pl.revo.commonhelpers.R;
import pl.revo.commonhelpers.internal.CodeAdapter.ItemViewHolder;
import pl.revo.commonhelpers.internal.CodeAdapter.ListInterface;
import pl.revo.commonhelpers.internal.KeyboardPresenter.KeyboardDataView;

public class CustomKeyboard extends LinearLayout implements ListInterface, KeyboardDataView {

	@BindViews({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
			R.id.delete, R.id.done})
	List<Button> actionButtonViews;
	@BindView(R.id.delete)
	Button deleteView;
	@BindView(R.id.done)
	Button doneView;
	@BindView(R.id.field)
	TextView fieldView;
//	@BindView(R.id.keyboard)
//	LinearLayout keyboardView;
	@BindView(R.id.codes_list)
	RecyclerView recyclerView;
	@BindColor(android.R.color.white)
	int transparent;
	CodeAdapter adapter;

	String[] codes = {"PL", "EN", "DE", "NL", "BL", "EH", "AH", "HA", "HA", "HA"};

	KeyboardPresenter presenter;

	public CustomKeyboard(Context context) {
		this(context, null);
	}

	public CustomKeyboard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomKeyboard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	void init() {
		inflate(getContext(), R.layout.widget_keyboard2, this);
		ButterKnife.bind(this, this);
		setOrientation(VERTICAL);
		setLayoutTransition(new LayoutTransition());
		presenter = new KeyboardPresenter(this);
		presenter.initialize(getContext(),actionButtonViews,transparent);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new CodeAdapter(this, codes);
		recyclerView.setAdapter(adapter);
		SnapHelper snapHelper = new LinearSnapHelper();
		snapHelper.attachToRecyclerView(recyclerView);
		if(fieldView.getText().length() >1) {
			String code = fieldView.getText().toString().substring(0, 2);
			for (int i =0 ; i< codes.length;i++) {
				if (code.equals(codes[i])) {
					adapter.setSelectedPosition(i);
					break;
				}
			}
		}
	}

	@OnClick(R.id.field)
	public void onFieldClick() {
		for (Button actionButtonView : actionButtonViews) {
			actionButtonView.setVisibility(VISIBLE);
		}
		recyclerView.setVisibility(VISIBLE);
//		keyboardView.setVisibility(VISIBLE);
	}

	@OnClick(R.id.done)
	public void onDoneClick() {
		for (Button actionButtonView : actionButtonViews) {
			actionButtonView.setVisibility(GONE);
		}
		recyclerView.setVisibility(GONE);
//		keyboardView.setVisibility(GONE);
	}

	@OnClick(R.id.delete)
	public void onDeleteClick() {
		fieldView.setText(removeLastChar(fieldView.getText()));
		presenter.publishText(fieldView.getText().toString());
	}

	@OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero})
	public void onActionClick(Button view) {
		fieldView.setText(String.format("%s%s", fieldView.getText(), view.getText().toString()));
		presenter.publishText(fieldView.getText().toString());
	}

	public boolean isCountryCodeSelected(){
		return presenter.getCountryCode() != null;
	}

	public String getText(){
		return presenter.getText();
	}

	private CharSequence removeLastChar(CharSequence text) {
		if (!TextUtils.isEmpty(text)) {
			return TextUtils.substring(text, 0, text.length() - 1);
		}
		return "";
	}

	private CharSequence addCodeString(CharSequence total, String code) {
		String cleanCode = total.toString().replaceAll("[*a-zA-Z ]", "");
		return code + cleanCode;
	}

	@Override
	public void onItemClick(ItemViewHolder holder) {
		presenter.setCountryCode(codes[holder.getAdapterPosition()]);
		fieldView.setText(addCodeString(fieldView.getText(),codes[holder.getAdapterPosition()]));
		onCountryCodeSelected(holder.getAdapterPosition());
		presenter.publishText(fieldView.getText().toString());
	}

	@Override
	public void onCountryCodeSelected(int position) {
		adapter.setSelectedPosition(position);
	}

	@Override
	public void onPossibleNumbersFound(List<Integer> positions, Drawable[] standardViewBgs, Drawable[] possibleViewBgs) {
		for (Button actionButtonView : actionButtonViews) {
			actionButtonView.setBackground(standardViewBgs[actionButtonViews.indexOf(actionButtonView)]);
			actionButtonView.invalidate();
			actionButtonView.requestLayout();
		}
		for (Integer position : positions) {
			actionButtonViews.get(position).setBackground(possibleViewBgs[position]);
			actionButtonViews.get(position).invalidate();
			actionButtonViews.get(position).requestLayout();
		}
	}

	public interface SuggestionItem {

		String code();
	}
}
