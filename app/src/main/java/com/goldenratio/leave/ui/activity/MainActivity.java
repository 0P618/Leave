package com.goldenratio.leave.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.adapter.FragmentPagerAdapter;
import com.goldenratio.leave.ui.fragment.HistoryFragment;
import com.goldenratio.leave.ui.fragment.LeaveFragment;
import com.goldenratio.leave.ui.fragment.MyFragment;
import com.goldenratio.leave.util.StatusBarUtil;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

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
    private FragmentPagerAdapter mMyFragmentPagerAdapter;
    private long exitTime = 0;

    private backListener mBackListener;

    //回调fragment的返回键
    public interface backListener {
        boolean callBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ShareSDK.initSDK(this);
    }

    private void initView() {
        mVpContent = (ViewPager) findViewById(R.id.vp_content);

        mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
        mRbLeave = (RadioButton) findViewById(R.id.rb_leave);
//        mRbNews = (RadioButton) findViewById(R.id.rb_news);
        mRbProgress = (RadioButton) findViewById(R.id.rb_history);
        mRbMy = (RadioButton) findViewById(R.id.rb_my);

        mFragmentList = new ArrayList<Fragment>();

        mBackListener = new LeaveFragment();
        mFragmentList.add((LeaveFragment) mBackListener);
//        mFragmentList.add(new NewsFragment());
        mFragmentList.add(new HistoryFragment());
        mFragmentList.add(new MyFragment());
        mMyFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);

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
                StatusBarUtil.setStatusBarColor(this, true, R.color.colorPrimary, false);
                mVpContent.setCurrentItem(0);
                break;
//            case R.id.rb_news:
//                mVpContent.setCurrentItem(1);
//                break;
            case R.id.rb_history:
                StatusBarUtil.setStatusBarColor(this, true, R.color.colorPrimary, false);
                mVpContent.setCurrentItem(1);
                break;
            case R.id.rb_my:
                StatusBarUtil.setStatusBarColor(this, true, R.color.colorSecondary, false);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mBackListener.callBack())
            return;
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次返回键退出",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //      finish();
            System.exit(0);
        }
    }
}
