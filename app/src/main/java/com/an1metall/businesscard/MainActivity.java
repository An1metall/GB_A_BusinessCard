package com.an1metall.businesscard;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private LinearLayout titleContainer;
    private TextView title;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private List<Card> cards;
    private Resources resources;

    private final String EXTRA_STRING_NAME = "lang";
    private final String LANGUAGE_CODE_RU = "ru";
    private final String LANGUAGE_CODE_EN = "en";
    private String language_code = LANGUAGE_CODE_RU;

    private final String LOG_TAG = "testlog";

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

        setContentView(R.layout.image_behavior);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        title = (TextView) findViewById(R.id.main_textview_title);
        titleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);

        toolbar.inflateMenu(R.menu.main_menu);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        resources = getResources();
        this.initializeData();

        rvAdapter = new RecyclerViewAdapter(this, cards);
        recyclerView.setAdapter(rvAdapter);
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
        Log.d(LOG_TAG, "changeLocale ran");
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
}
