package com.an1metall.businesscard;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Card> cards;

    RecyclerViewAdapter(List<Card> cards){
        this.cards = cards;
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.cardName.setText(cards.get(i).name);
        viewHolder.cardData.setText(cards.get(i).data);
        viewHolder.cardPic.setImageResource(cards.get(i).picID);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
    }
}
