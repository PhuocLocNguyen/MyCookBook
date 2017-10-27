package com.example.kaildyhoang.mycookbookapplication.view;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import com.example.kaildyhoang.mycookbookapplication.R;

import static com.example.kaildyhoang.mycookbookapplication.R.id.viewpager;


public class MainActivity_View extends FragmentActivity {
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getSupportActionBar().hide();
        setContentView(R.layout.v_activity_mainview);
        viewPager = (ViewPager) findViewById(viewpager);

        Adapter_ViewPagerFragment adapter_viewPagerFragment =new Adapter_ViewPagerFragment(getSupportFragmentManager(),3);
        viewPager.setAdapter(adapter_viewPagerFragment);

        viewPager.setCurrentItem(1);
    }

    public ViewPager getViewPager(View view) {
        if (null == viewPager) {
            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        }
        return viewPager;
    }


}