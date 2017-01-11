package com.collage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.collage.util.HomeFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_tab_layout)
    TabLayout homeTabLayout;

    @BindView(R.id.home_view_pager)
    ViewPager homeViewPager;

    @BindView(R.id.home_toolbar)
    Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(homeToolbar);

        HomeFragmentPagerAdapter homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        homeViewPager.setAdapter(homeFragmentPagerAdapter);
        homeViewPager.setOffscreenPageLimit(homeFragmentPagerAdapter.getCount());
        homeTabLayout.setupWithViewPager(homeViewPager);
        homeFragmentPagerAdapter.setUpTabIcons(homeTabLayout);

    }
}
