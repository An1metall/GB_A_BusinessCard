package com.an1metall.businesscard;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Card item, int position);
    }

    List<Card> cards;

    private int lastPosition = -1;

    private Context context;
    private final OnItemClickListener listener;

    RecyclerViewAdapter(Context context, List<Card> cards, OnItemClickListener listener) {
        this.context = context;
        this.cards = cards;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindItem(cards.get(position), position, listener);
        setAnimation(viewHolder.cardView, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            lastPosition = position;

            view.setTranslationY(getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    private static int getScreenHeight(Context c) {
        int screenHeight = 0;
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView cardName;
        TextView cardData;
        ImageView cardPic;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            cardName = (TextView) itemView.findViewById(R.id.card_name);
            cardData = (TextView) itemView.findViewById(R.id.card_data);
            cardPic = (ImageView) itemView.findViewById(R.id.card_pic);
        }

        public void bindItem(final Card item, final int position, final OnItemClickListener listener){
            cardName.setText(item.getName());
            cardData.setText(item.getData());
            cardPic.setImageResource(item.getPicID());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(position);
                    listener.onItemClick(item, position);
                }
            });
        }
    }
}
