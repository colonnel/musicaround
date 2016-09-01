package ua.asd.musicaround;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    private PresentationInfo[] presentationInfo;


    public MyFragmentPageAdapter(FragmentManager fm,PresentationInfo[] presentationInfo ) {
        super(fm);
        this.presentationInfo = presentationInfo;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newIntent(presentationInfo[position]);
    }

    @Override
    public int getCount() {
        return presentationInfo.length;
    }
}
