package com.an1metall.businesscard;

import android.content.ComponentName;
import android.content.Context;
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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.daimajia.swipe.util.Attributes;

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
    private Intent intent;
    private String uri;
    PhoneCallListener phoneListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();

        ActivityLocale.setLocale(this, getIntent());
        setContentView(R.layout.activity_main);
        bindActivity();
        initializeData();

        toolbar.inflateMenu(R.menu.main_menu);

        recyclerView.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
        rvAdapter = new RecyclerViewAdapter(this, cards, this);
        ((RecyclerViewAdapter) rvAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(rvAdapter);

        AppBarAnimation.startAlphaAnimation(title, 0, View.INVISIBLE);
        appBarLayout.addOnOffsetChangedListener(new AppBarAnimation(title));
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
        TypedArray cardPicsId = resources.obtainTypedArray(R.array.card_pics);
        TypedArray cardBtnPicsId = resources.obtainTypedArray(R.array.card_btn);
        for (int i = 0; i < names.length; i++) {
            cards.add(new Card(names[i], data[i], cardPicsId.getResourceId(i, 0), cardBtnPicsId.getResourceId(i, 0)));
        }
        cardPicsId.recycle();
        cardBtnPicsId.recycle();
    }

    public void onMenuItemClick(MenuItem item) {
        ActivityLocale.changeLocale(this, getIntent());
    }

    @Override
    public void onItemClick(Card item, int position) {
        intent = null;
        uri = item.getData();
        switch (position) {
            case CardType.PHONE:
                makePhoneCall();
                break;
            case CardType.EMAIL:
                sendViaEmail();
                break;
            case CardType.SKYPE:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("skype:" + uri));
                intent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case CardType.GITHUB:
                uri = getResources().getString(R.string.github_url);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                break;
            case CardType.LINKEDIN:
                uri = getResources().getString(R.string.linkedin_url);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                break;
            case CardType.SKILLS:
                intent = new Intent(this, SkillsActivity.class);
                break;
        }
        if (intent != null)
            if (isCallable(intent)) {
                startActivity(intent);
            } else showToast(getString(R.string.not_supported));
    }

    private void sendViaEmail() {
        if (isCallable(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")))) {
            Bundle bundle = new Bundle();
            bundle.putString("uri", uri);

            DatePickerDialogFragment dialog = new DatePickerDialogFragment();
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "DATE_PICKER");
        } else {
            showToast(getString(R.string.not_supported));
        }
    }

    private void makePhoneCall() {
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + uri));
        phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
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

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                if (isPhoneCalling) {
                    intent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    isPhoneCalling = false;
                }

            }
        }
    }

}
