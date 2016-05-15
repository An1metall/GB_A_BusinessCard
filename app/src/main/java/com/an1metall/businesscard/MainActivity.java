package com.an1metall.businesscard;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private FloatingActionButton fab;
    private List<Card> cards;
    private Resources resources;

    private final String EXTRA_STRING_NAME = "lang";
    private final String LANGUAGE_CODE_RU = "ru";
    private final String LANGUAGE_CODE_EN = "en";
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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        resources = getResources();
        this.initializeData();

        rvAdapter = new RecyclerViewAdapter(cards);
        recyclerView.setAdapter(rvAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (language_code.equals(LANGUAGE_CODE_RU)) {
            fab.setImageResource(R.drawable.en);
        } else {
            fab.setImageResource(R.drawable.ru);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLocale();
            }
        });
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

    private void changeLocale() {
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
