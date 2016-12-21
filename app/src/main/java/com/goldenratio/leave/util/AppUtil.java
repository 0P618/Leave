package com.goldenratio.leave.util;

import android.content.Context;

/**
 * Created by Kiuber on 2016/12/21.
 */

public class AppUtil {
    // 用户点击跳转登录界面 RequestCode
    public static final int USER_LOGIN = 1;
    // 用户点击跳转设置界面 RequestCode
    public static final int APP_SETTING = 2;

    public static String isLogin(Context context) {
        String login_state = SharedPreferenceUtil.getOne(context, "app_config", "login_state");
        if (!login_state.equals("")) {
            String state = login_state.substring(0, 1);
            if (state.equals("1")) {
                String id = SharedPreferenceUtil.getOne(context, "user_info", "id");
                if (!id.equals("")) {
                    String idMD5 = MD5Util.createMD5(id);
                    if (idMD5.equals(login_state.substring(1))) {
                        return id;
                    } else {
                        // app_config中的idMD5与user_info的idMD5不符
                        return null;
                    }
                } else {
                    // id == ""
                    return null;
                }
            } else {
                // 未登录或者未保持登录
                return null;
            }
        } else {
            //login_state == ""
            return null;
        }
    }
}
