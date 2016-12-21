package com.goldenratio.leave.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.goldenratio.leave.R;
import com.goldenratio.leave.adapter.MyFragmentPagerAdapter;
import com.goldenratio.leave.ui.fragment.LeaveFragment;
import com.goldenratio.leave.ui.fragment.MyFragment;
import com.goldenratio.leave.ui.fragment.NewsFragment;
import com.goldenratio.leave.ui.fragment.ProgressFragment;

import java.util.ArrayList;

/**
 * Created by Kiuber on 2016/12/19.
 */

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private ViewPager mVpContent;
    private RadioGroup mRgTabs;
    private RadioButton mRbLeave;
    //    private RadioButton mRbNews;
    private RadioButton mRbProgress;
    private RadioButton mRbMy;
    private ArrayList<Fragment> mFragmentList;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mVpContent = (ViewPager) findViewById(R.id.vp_content);

        mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
        mRbLeave = (RadioButton) findViewById(R.id.rb_leave);
//        mRbNews = (RadioButton) findViewById(R.id.rb_news);
        mRbProgress = (RadioButton) findViewById(R.id.rb_progress);
        mRbMy = (RadioButton) findViewById(R.id.rb_my);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new LeaveFragment());
//        mFragmentList.add(new NewsFragment());
        mFragmentList.add(new ProgressFragment());
        mFragmentList.add(new MyFragment());
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);

        mRgTabs.setOnCheckedChangeListener(this);
        mRbLeave.setChecked(true);
//        mRbNews.setChecked(true);

        mVpContent.setAdapter(mMyFragmentPagerAdapter);
        mVpContent.setCurrentItem(0);
        mVpContent.addOnPageChangeListener(this);
        mVpContent.setOffscreenPageLimit(3);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_leave:
                mVpContent.setCurrentItem(0);
                break;
//            case R.id.rb_news:
//                mVpContent.setCurrentItem(1);
//                break;
            case R.id.rb_progress:
                mVpContent.setCurrentItem(1);
                break;
            case R.id.rb_my:
                mVpContent.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            //滑动已完成
            switch (mVpContent.getCurrentItem()) {
                case 0:
                    mRbLeave.setChecked(true);
                    break;
//                case 1:
//                    mRbNews.setChecked(true);
//                    break;
                case 1:
                    mRbProgress.setChecked(true);
                    break;
                case 2:
                    mRbMy.setChecked(true);
                    break;
            }
        }
    }
}