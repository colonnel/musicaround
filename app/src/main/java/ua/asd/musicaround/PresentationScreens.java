package ua.asd.musicaround;

import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;


public class PresentationScreens extends FragmentActivity implements View.OnClickListener {


    private static final String TAG = "myLogs";

    ViewPager pager;
    PagerAdapter pagerAdapter;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_screens);
        pagerAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), PresentationInfo.values());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(this);
        // Add Pager indicator
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.titles);
        circlePageIndicator.setViewPager(pager);


/*
Created Page Change Listener
 */
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position =" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next:
                if (pager.getCurrentItem() < pager.getChildCount() - 1) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                break;
        }
    }

}