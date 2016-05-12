package com.an1metall.businesscard;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private FloatingActionButton fab;
    private List<Card> cards;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        resources = getResources();
        initializeData();

        rvAdapter = new RecyclerViewAdapter(cards);
        recyclerView.setAdapter(rvAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        String locale = this.getResources().getConfiguration().locale.getDisplayName();
        if (locale.contains("Russian")) {
            fab.setImageResource(R.drawable.ru);
        } else {
            fab.setImageResource(R.drawable.en);
        }
/*        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLocale();
            }
        });*/
    }

    private void initializeData() {
        cards = new ArrayList<>();
        String[] names = resources.getStringArray(R.array.card_names);
        String[] data = resources.getStringArray(R.array.card_data);
        TypedArray picsId = resources.obtainTypedArray(R.array.card_pics);
        for (int i = 0; i < names.length; i++) {
            cards.add(new Card(names[i], data[i], picsId.getResourceId(i,0)));
        }
    }
}
