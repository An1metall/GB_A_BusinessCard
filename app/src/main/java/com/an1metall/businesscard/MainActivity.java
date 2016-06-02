package com.an1metall.businesscard;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private TextView title;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private List<Card> cards;
    private Resources resources;
    private Toast currentToast;

    private boolean titleVisible = false;

    private static final String EXTRA_STRING_NAME = "lang";
    private static final String LANGUAGE_CODE_RU = "ru";
    private static final String LANGUAGE_CODE_EN = "en";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private String language_code = LANGUAGE_CODE_RU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();

        setLocale();
        setContentView(R.layout.activity_main);
        bindActivity();
        initializeData();

        toolbar.inflateMenu(R.menu.main_menu);

        recyclerView.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
        rvAdapter = new RecyclerViewAdapter(this, cards, this);
        recyclerView.setAdapter(rvAdapter);

        startAlphaAnimation(title, 0, View.INVISIBLE);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleToolbarTitleVisibility(percentage);
            }
        });
    }

    private void bindActivity() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void initializeData() {
        cards = new ArrayList<>();
        String[] names = resources.getStringArray(R.array.card_names);
        String[] data = resources.getStringArray(R.array.card_data);
        TypedArray picsId = resources.obtainTypedArray(R.array.card_pics);
        for (int i = 0; i < names.length; i++) {
            cards.add(new Card(names[i], data[i], picsId.getResourceId(i, 0)));
        }
        picsId.recycle();
    }

    private void setLocale(){
        Intent intent = getIntent();
        if (intent.getStringExtra(EXTRA_STRING_NAME) != null) {
            language_code = intent.getStringExtra(EXTRA_STRING_NAME);
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        android.content.res.Configuration conf = resources.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        resources.updateConfiguration(conf, dm);
    }

    public void changeLocale(MenuItem item) {
        Intent intent = getIntent();
        if (language_code.equals(LANGUAGE_CODE_RU)) {
            language_code = LANGUAGE_CODE_EN;
        } else {
            language_code = LANGUAGE_CODE_RU;
        }
        intent.putExtra(EXTRA_STRING_NAME, language_code);
        finish();
        startActivity(intent);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!titleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                titleVisible = true;
            }
        } else {
            if (titleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                titleVisible = false;
            }
        }
    }

    private static void startAlphaAnimation(View view, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    @Override
    public void onItemClick(Card item, int position) {
        switch (position) {
            case 1:
                sendViaEmail(item.getData());
                break;
            case 2:
                sendViaSkype(item.getData());
                break;
        }
    }

    private void sendViaEmail(String uri) {
        if (isCallable(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:")))) {
            Bundle bundle = new Bundle();
            bundle.putString("uri", uri);

            DatePickerDialogFragment dialog = new DatePickerDialogFragment();
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "DATE_PICKER");
        }
        else {
            showToast(getString(R.string.email_account));
        }
    }

    private void sendViaSkype(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("skype:" + uri));
        intent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isCallable(intent)) {
            startActivity(intent);
        } else {
            showToast(getString(R.string.skype_not_found));
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void showToast(String text) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        currentToast.show();
    }

}
