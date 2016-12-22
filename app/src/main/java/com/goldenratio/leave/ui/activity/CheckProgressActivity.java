package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.goldenratio.leave.R;

import java.util.ArrayList;
import java.util.List;

public class CheckProgressActivity extends Activity {

    private HorizontalStepView mStepView;
    private TextView mTvTitle;
    private TextView mTvPoint;
    private TextView mTName;
    private TextView mTClassType;
    private TextView mTTime;
    private TextView mTContext;
    private Button mBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_progress);
        initView();
        initData();
    }

    private void initData() {
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("提交申请",1);
        StepBean stepBean1 = new StepBean("班主任确认",1);
        StepBean stepBean2 = new StepBean("辅导员确认",1);
        StepBean stepBean3 = new StepBean("系主任确认",0);
        StepBean stepBean4 = new StepBean("完成",-1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);

        mStepView
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(10)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon
    }

    private void initView() {
        mStepView = (HorizontalStepView) findViewById(R.id.step_view);
        mTvPoint = (TextView) findViewById(R.id.tv_point);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvPoint = (TextView) findViewById(R.id.tv_point);
        mTName = (TextView) findViewById(R.id.t_name);
        mTClassType = (TextView) findViewById(R.id.t_class_type);
        mTTime = (TextView) findViewById(R.id.t_time);
        mTContext = (TextView) findViewById(R.id.t_context);
        mBtnClose = (Button) findViewById(R.id.btn_close);

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
