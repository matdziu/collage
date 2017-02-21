package pl.collage.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.collage.R;
import pl.collage.base.BaseActivity;
import pl.collage.util.adapters.HomeFragmentPagerAdapter;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_tab_layout)
    TabLayout homeTabLayout;

    @BindView(R.id.home_view_pager)
    ViewPager homeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        HomeFragmentPagerAdapter homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        homeViewPager.setAdapter(homeFragmentPagerAdapter);
        homeViewPager.setOffscreenPageLimit(homeFragmentPagerAdapter.getCount());
        homeTabLayout.setupWithViewPager(homeViewPager);
        homeFragmentPagerAdapter.setUpTabIcons(homeTabLayout);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    public void hideHomeNavigation() {
        super.hideHomeNavigation();
        homeTabLayout.setVisibility(View.GONE);
    }

    @Override
    public void showHomeNavigation() {
        super.showHomeNavigation();
        homeTabLayout.setVisibility(View.VISIBLE);
    }

    public void navigateToGalleryFragment(int galleryFragmentPosition) {
        homeViewPager.setCurrentItem(galleryFragmentPosition, true);
    }

    public void navigateToFriendsFragment(int friendsFragmentPosition) {
        homeViewPager.setCurrentItem(friendsFragmentPosition, true);
    }
}
