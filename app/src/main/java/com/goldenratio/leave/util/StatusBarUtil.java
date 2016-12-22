package com.goldenratio.leave.util;

import android.app.Activity;

import com.goldenratio.leave.util.statusbar.XIAOMI;

import static com.goldenratio.leave.util.AppUtil.ROM_XIAOMI;

/**
 * Created by Kiuber on 2016/12/21.
 */

public class StatusBarUtil {
    /**
     * 设置状态栏
     *
     * @param activity
     * @param on              true-->在状态栏后面,false-->在状态栏下面
     * @param color           状态栏颜色
     * @param isFontColorDark true-->状态栏字体黑色
     */
    public static void setStatusBarColor(Activity activity, boolean on, int color, boolean isFontColorDark) {
        String phoneRom = AppUtil.getPhoneRom();
        switch (phoneRom) {
            case ROM_XIAOMI:
                XIAOMI.setStatusBarColor(activity, on, color, isFontColorDark);
                break;
        }
    }
}
