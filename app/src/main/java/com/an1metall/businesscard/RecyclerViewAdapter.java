package com.an1metall.businesscard;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Card> cards;

    private int lastPosition = -1;
    private Context context;

    RecyclerViewAdapter(Context context, List<Card> cards) {
        this.context = context;
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
        setAnimation(viewHolder.cardView, i);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView cardName;
        TextView cardData;
        ImageView cardPic;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            cardName = (TextView) itemView.findViewById(R.id.card_name);
            cardData = (TextView) itemView.findViewById(R.id.card_data);
            cardPic = (ImageView) itemView.findViewById(R.id.card_pic);
        }

        @Override
        public void onClick(View view) {
            switch (getLayoutPosition()) {
                case 1:
                    sendViaEmail(cardData.getText().toString());
                    break;
                case 2:
                    sendViaSkype(cardData.getText().toString());
                    break;
            }
        }

        public void sendViaEmail(String uri) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", uri, null));
            context.startActivity(Intent.createChooser(intent, "Send Email"));
        }

        public void sendViaSkype(String uri) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("skype:" + uri));
                intent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Skype not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
