package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.util.AppUtil;
import com.goldenratio.leave.util.GlobalVariable;
import com.goldenratio.leave.util.SharedPreferenceUtil;
import com.goldenratio.leave.util.StatusBarUtil;

/**
 * Created by Kiuber on 2016/12/21.
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        StatusBarUtil.setStatusBarColor(this, true, R.color.colorPrimary, false);
    }

    private void initView() {
        findViewById(R.id.tv_exit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                String id = AppUtil.isLogin(SettingActivity.this);
                if (id != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setMessage("是否退出？");
                    builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearUserInfo();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                } else {
                    Toast.makeText(this, "您尚未登陆", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void clearUserInfo() {
        SharedPreferenceUtil.putOne(SettingActivity.this, "app_config", "login_state", "");
        getSharedPreferences(GlobalVariable.FILE_NAME_USER_INFO, MODE_PRIVATE).edit().clear().apply();
    }
}
