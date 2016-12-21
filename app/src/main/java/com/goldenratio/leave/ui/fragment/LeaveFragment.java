package com.goldenratio.leave.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.goldenratio.leave.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Kiuber on 2016/12/18.
 */

public class LeaveFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TimePickerView pvTime;
    private AlertDialog leaveTypeDialog;

    private RelativeLayout mRlType;
    private TextView mTvType;
    private RelativeLayout mRlStartTime;
    private TextView mTvStartTime;
    private RelativeLayout mRlEndTime;
    private TextView mTvEndTime;
    private TextView mTvDays;
    private EditText mEtDays;
    private EditText mEtWhy;
    private Button mBtnSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leave, null);

        initView();
        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);

        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        return view;
    }

    private String getTime(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void initView() {
        mRlType = (RelativeLayout) view.findViewById(R.id.rl_type);
        mTvType = (TextView) view.findViewById(R.id.tv_type);
        mRlStartTime = (RelativeLayout) view.findViewById(R.id.rl_start_time);
        mTvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        mRlEndTime = (RelativeLayout) view.findViewById(R.id.rl_end_time);
        mTvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        mTvDays = (TextView) view.findViewById(R.id.tv_days);
        mEtDays = (EditText) view.findViewById(R.id.et_days);
        mEtWhy = (EditText) view.findViewById(R.id.et_why);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);

        mRlType.setOnClickListener(this);
        mRlStartTime.setOnClickListener(this);
        mRlEndTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_type:
                showTypeAlertDialog();
                break;
            case R.id.rl_start_time:
                //时间选择后回调
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        mTvStartTime.setText(getTime(date));
                    }
                });
                pvTime.show();               //弹出时间选择器
                break;
            case R.id.rl_end_time:
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        mTvEndTime.setText(getTime(date));
                    }
                });
                pvTime.show();
                break;
            case R.id.btn_submit:
                break;
            default:
                break;
        }
    }


    public void showTypeAlertDialog() {
        final String[] items = {"病假", "事假", "其它"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("请假类型");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                mTvType.setText(items[index]);
                leaveTypeDialog.dismiss();
            }
        });
        leaveTypeDialog = alertBuilder.create();
        leaveTypeDialog.show();
    }
}
