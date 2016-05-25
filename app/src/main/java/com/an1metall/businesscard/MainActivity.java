package com.an1metall.businesscard;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private TextView title;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private List<Card> cards;
    private Resources resources;

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

        Intent intent = getIntent();
        if (intent.getStringExtra(EXTRA_STRING_NAME) != null) {
            language_code = intent.getStringExtra(EXTRA_STRING_NAME);
        }

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);

        setContentView(R.layout.activity_main);
        bindActivity();

        toolbar.inflateMenu(R.menu.main_menu);

        recyclerView.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        resources = getResources();
        this.initializeData();

        rvAdapter = new RecyclerViewAdapter(this, cards);
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

    public static void startAlphaAnimation(View view, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }
}
