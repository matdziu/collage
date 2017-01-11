package com.collage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.collage.util.MainFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(mainFragmentPagerAdapter);
        mainViewPager.setOffscreenPageLimit(mainFragmentPagerAdapter.getCount());
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainFragmentPagerAdapter.setUpTabIcons(mainTabLayout);

    }
}
