package com.collage.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.collage.R;
import com.collage.interactors.FirebaseAuthInteractor;
import com.collage.util.HomeFragmentPagerAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_tab_layout)
    TabLayout homeTabLayout;

    @BindView(R.id.home_view_pager)
    ViewPager homeViewPager;

    @BindView(R.id.home_toolbar)
    Toolbar homeToolbar;

    private SystemBarTintManager tintManager;
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        homePresenter = new HomePresenter(new FirebaseAuthInteractor());

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#20000000"));

        homeToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(homeToolbar);

        HomeFragmentPagerAdapter homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        homeViewPager.setAdapter(homeFragmentPagerAdapter);
        homeViewPager.setOffscreenPageLimit(homeFragmentPagerAdapter.getCount());
        homeTabLayout.setupWithViewPager(homeViewPager);
        homeFragmentPagerAdapter.setUpTabIcons(homeTabLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                homePresenter.signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hideHomeNavigation() {
        homeToolbar.setVisibility(View.GONE);
        homeTabLayout.setVisibility(View.GONE);
        tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    public void showHomeNavigation() {
        homeToolbar.setVisibility(View.VISIBLE);
        homeTabLayout.setVisibility(View.VISIBLE);
        tintManager.setTintColor(Color.parseColor("#20000000"));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
