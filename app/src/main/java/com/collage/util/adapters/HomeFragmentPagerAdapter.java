package com.collage.util.adapters;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.collage.R;
import com.collage.camera.CameraFragment;
import com.collage.gallery.GalleryFragment;
import com.collage.friends.FriendsFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private int[] tabImageResId = {
            R.drawable.ic_friends,
            R.drawable.ic_gallery,
            R.drawable.ic_camera
    };

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FriendsFragment();
            case 1:
                return new GalleryFragment();
            case 2:
                return new CameraFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @SuppressWarnings("ConstantConditions")
    public void setUpTabIcons(TabLayout tabLayout) {
        for (int position = 0; position < tabLayout.getTabCount(); position++) {
            tabLayout.getTabAt(position).setIcon(tabImageResId[position]);
        }
    }
}
