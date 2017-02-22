package pl.revo.commonhelpers.internal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.ButterKnife;
import java.util.Arrays;
import java.util.List;
import pl.revo.commonhelpers.R;
import pl.revo.commonhelpers.internal.CodeAdapter.ItemViewHolder;

public class CodeAdapter extends RecyclerView.Adapter<ItemViewHolder> {

	ListInterface listener;
	List<String> codeList;
	int selectedPosition = -1;

	public CodeAdapter(ListInterface listener, String[] data) {
		this(listener, Arrays.asList(data));
	}

	public CodeAdapter(ListInterface listener, List<String> data) {
		this.listener = listener;
		this.codeList = data;
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemViewHolder(parent, LayoutInflater.from(parent.getContext()), listener);
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		holder.bindData(codeList.get(position),selectedPosition == position);
	}

	@Override
	public int getItemCount() {
		return codeList.size();
	}

	public void setSelectedPosition(int adapterPosition) {
		this.selectedPosition = adapterPosition;
		notifyDataSetChanged();
	}

	public static class ItemViewHolder extends RecyclerView.ViewHolder {

		private final ListInterface listener;
		TextView textView;
		CardView cardView;
		@BindColor(R.color.colorAccent) int mSelected;
		@BindColor(android.R.color.transparent) int mUnselected;

		public ItemViewHolder(ViewGroup parent, LayoutInflater inflater, ListInterface listInterface) {
			super(inflater.inflate(R.layout.item_view, parent, false));
			ButterKnife.bind(this,itemView);
			textView = (TextView) itemView.findViewById(R.id.code_title);
			cardView = (CardView) itemView.findViewById(R.id.card);
			this.listener = listInterface;
			itemView.setOnClickListener(view -> {
				if (listInterface != null) {
					listInterface.onItemClick(ItemViewHolder.this);
				}
			});
		}

		public void bindData(String text,boolean selected) {
			textView.setText(text);
			cardView.setVisibility(selected ? View.VISIBLE : View.GONE);
//			textView.setBackground(DrawableUtils.builder().orientation(Orientation.BOTTOM_TOP)
//					.color(selected ? mSelected : mUnselected)
//					.secondColor(selected ? mSelected : mUnselected)
//					.cornerRadius(textView.getWidth()/2)
//					.draw());
		}
	}

	public interface ListInterface {
		void onItemClick(ItemViewHolder holder);
	}
}
