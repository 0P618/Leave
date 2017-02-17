package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.goldenratio.leave.R;
import com.goldenratio.leave.util.GlobalConstant;
import com.goldenratio.leave.util.SharedPreferenceUtil;
import com.goldenratio.leave.util.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kiuber on 2017/2/16 0016.
 */

public class PersonActivity extends Activity implements View.OnClickListener {

    private TextView mTvNickname;
    private TextView mTvAutograph;
    private TextView mTvSex;
    private TextView mTvBirthday;
    private TextView mTvHometown;
    private TextView mTvHighSchool;
    private TextView mTvTel;
    private TextView mTvMail;
    private TextView mTvQQ;
    private TextView mTvWechat;
    private TextView mTvId;
    private TextView mTvName;
    private TextView mTvClass;
    private TextView mTvDepartment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        StatusBarUtil.setStatusBarColor(this, true, R.color.colorPrimary, false);
        initView();
        initData();
    }

    private void initData() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("nickname");
        strings.add("autograph");
        strings.add("sex");
        strings.add("birthday");
        strings.add("hometown");
        strings.add("high_school");
        strings.add("tel");
        strings.add("mail");
        strings.add("qq");
        strings.add("wechat");
        strings.add("id");
        strings.add("name");
        strings.add("aClass");
        strings.add("department");
        ArrayList<String> multiple = SharedPreferenceUtil.getMultiple(this, GlobalConstant.FILE_NAME_USER_INFO, strings);

        mTvNickname.setText(multiple.get(0));
        mTvAutograph.setText(multiple.get(1));
        mTvSex.setText(multiple.get(2));
        mTvBirthday.setText(multiple.get(3));
        mTvHometown.setText(multiple.get(4));
        mTvHighSchool.setText(multiple.get(5));
        mTvTel.setText(multiple.get(6));
        mTvMail.setText(multiple.get(7));
        mTvQQ.setText(multiple.get(8));
        mTvWechat.setText(multiple.get(9));

        mTvId.setText(multiple.get(10));
        mTvName.setText(multiple.get(11));
        mTvClass.setText(multiple.get(12));
        mTvDepartment.setText(multiple.get(13));
    }

    private void initView() {
        mTvNickname = (TextView) findViewById(R.id.tv_nickname);
        mTvAutograph = (TextView) findViewById(R.id.tv_autograph);
        mTvSex = (TextView) findViewById(R.id.tv_sex);
        mTvBirthday = (TextView) findViewById(R.id.tv_birthday);
        mTvHometown = (TextView) findViewById(R.id.tv_hometown);
        mTvHighSchool = (TextView) findViewById(R.id.tv_high_school);
        mTvTel = (TextView) findViewById(R.id.tv_tel);
        mTvMail = (TextView) findViewById(R.id.tv_mail);
        mTvQQ = (TextView) findViewById(R.id.tv_qq);
        mTvWechat = (TextView) findViewById(R.id.tv_wechat);

        mTvId = (TextView) findViewById(R.id.tv_id);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvClass = (TextView) findViewById(R.id.tv_class);
        mTvDepartment = (TextView) findViewById(R.id.tv_department);

        mTvNickname.setOnClickListener(this);
        mTvAutograph.setOnClickListener(this);
        mTvSex.setOnClickListener(this);
        mTvBirthday.setOnClickListener(this);
        mTvHometown.setOnClickListener(this);
        mTvHighSchool.setOnClickListener(this);
        mTvTel.setOnClickListener(this);
        mTvMail.setOnClickListener(this);
        mTvQQ.setOnClickListener(this);
        mTvWechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_nickname:
                showChangeDialog("昵称", mTvNickname);
                break;
            case R.id.tv_autograph:
                showChangeDialog("个签", mTvAutograph);
                break;
            case R.id.tv_sex:
                showChangeDialog("性别", mTvSex);
                break;
            case R.id.tv_birthday:
                TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
                timePickerView.show();
                timePickerView.setCancelable(true);
                timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        mTvBirthday.setText(simpleDateFormat.format(date));
                    }
                });
                break;
            case R.id.tv_hometown:
                showChangeDialog("家乡", mTvHometown);
                break;
            case R.id.tv_high_school:
                showChangeDialog("毕业高中", mTvHighSchool);
                break;
            case R.id.tv_tel:
                showChangeDialog("手机", mTvTel);
                break;
            case R.id.tv_mail:
                showChangeDialog("邮箱", mTvMail);
                break;
            case R.id.tv_qq:
                showChangeDialog("QQ", mTvQQ);
                break;
            case R.id.tv_wechat:
                showChangeDialog("微信", mTvWechat);
                break;
        }
    }

    private void showChangeDialog(String type, final TextView textView) {
        final AppCompatEditText appCompatEditText = new AppCompatEditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        appCompatEditText.setLayoutParams(layoutParams);
        appCompatEditText.setHint(textView.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入新的" + type);
        builder.setView(appCompatEditText);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(appCompatEditText.getText().toString());
            }
        });
        builder.show();
    }
}

