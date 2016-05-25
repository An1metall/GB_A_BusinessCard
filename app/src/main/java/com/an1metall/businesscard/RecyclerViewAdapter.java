package com.an1metall.businesscard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Card> cards;

    private int lastPosition = -1;
    private AppCompatActivity activity;

    RecyclerViewAdapter(AppCompatActivity activity, List<Card> cards) {
        this.activity = activity;
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

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            lastPosition = position;

            view.setTranslationY(getScreenHeight(activity));
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView cardName;
        TextView cardData;
        ImageView cardPic;
        private Toast currentToast;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            cardName = (TextView) itemView.findViewById(R.id.card_name);
            cardData = (TextView) itemView.findViewById(R.id.card_data);
            cardPic = (ImageView) itemView.findViewById(R.id.card_pic);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            notifyItemChanged(position);
            switch (position) {
                case 1:
                    sendViaEmail(cardData.getText().toString());
                    break;
                case 2:
                    sendViaSkype(cardData.getText().toString());
                    break;
            }
        }

        private void sendViaEmail(String uri) {
            if (isCallable(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:")))) {
                Bundle bundle = new Bundle();
                bundle.putString("uri", uri);

                DatePickerDialogFragment dialog = new DatePickerDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(activity.getSupportFragmentManager(), "DATE_PICKER");
            }
            else {
                showToast(activity.getString(R.string.email_account));
            }
        }

        private void sendViaSkype(String uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("skype:" + uri));
            intent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (isCallable(intent)) {
                activity.startActivity(intent);
            } else {
                showToast(activity.getString(R.string.skype_not_found));
            }
        }

        private boolean isCallable(Intent intent) {
            List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }

        public void showToast(String text) {
            if (currentToast != null) {
                currentToast.cancel();
            }
            currentToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
            currentToast.show();
        }
    }
}
