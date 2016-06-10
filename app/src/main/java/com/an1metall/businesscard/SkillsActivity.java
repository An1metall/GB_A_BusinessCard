package com.an1metall.businesscard;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gigamole.library.ntb.NavigationTabBar;

import java.util.ArrayList;


public class SkillsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;

    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLocale.setLocale(this, getIntent());
        setContentView(R.layout.activity_skills);
        bindActivity();

        resources = getResources();

        toolbar.inflateMenu(R.menu.main_menu);

        setupViewPager();
        setupNavigationTabBar();
    }

    private void bindActivity() {
        toolbar = (Toolbar) findViewById(R.id.skills_toolbar);
        viewPager = (ViewPager) findViewById(R.id.skills_viewpager);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_vertical);
    }

    public void onMenuItemClick(MenuItem item) {
        ActivityLocale.changeLocale(this, getIntent());
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFragmentInfo(), resources.getString(R.string.info_fragment));
        adapter.addFragment(new TabFragmentLanguages(), resources.getString(R.string.languages_fragment));
        adapter.addFragment(new TabFragmentTools(), resources.getString(R.string.tools_fragment));
        adapter.addFragment(new TabFragmentOS(), resources.getString(R.string.os_fragment));
        viewPager.setAdapter(adapter);
    }

    private void setupNavigationTabBar(){
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final String[] colors = getResources().getStringArray(R.array.polluted_waves);

        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.tab_info),
                        Color.parseColor(colors[0]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.tab_language),
                        Color.parseColor(colors[1]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.tab_tools),
                        Color.parseColor(colors[2]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.tab_os),
                        Color.parseColor(colors[3]))
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setBehaviorEnabled(true);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });
    }

}
