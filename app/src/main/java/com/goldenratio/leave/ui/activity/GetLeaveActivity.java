package com.goldenratio.leave.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.goldenratio.leave.R;

public class GetLeaveActivity extends AppCompatActivity {

    private TextView mName;
    private TextView mClassName;
    private TextView msTime;
    private TextView meTime;
    private TextView mContent;
    private TextView mIsSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfack);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mName.setText("姓名：" + intent.getStringExtra("name"));
        mClassName.setText("班级：" + intent.getStringExtra("className"));
        msTime.setText("开始时间：" + intent.getStringExtra("Stime"));
        meTime.setText("结束时间：" + intent.getStringExtra("Etime"));
        mContent.setText("详情：" + intent.getStringExtra("context"));
        mIsSuccess.setText("审核情况：" + intent.getStringExtra("isSuccess"));
    }

    private void initView() {
        mName = (TextView) findViewById(R.id.name);
        mClassName = (TextView) findViewById(R.id.className);
        msTime = (TextView) findViewById(R.id.sTime);
        meTime = (TextView) findViewById(R.id.eTime);
        mContent = (TextView) findViewById(R.id.content);
        mIsSuccess = (TextView) findViewById(R.id.isTrue);
    }
}
