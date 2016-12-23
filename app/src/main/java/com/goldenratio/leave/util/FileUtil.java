package com.goldenratio.leave.util;

import android.os.Environment;

import java.util.Objects;

/**
 * Created by Kiuber on 2016/12/23.
 */

public class FileUtil {


    public static boolean isSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
