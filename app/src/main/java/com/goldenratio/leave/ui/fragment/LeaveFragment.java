package com.goldenratio.leave.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.goldenratio.leave.R;
import com.goldenratio.leave.bean.LeaveBean;
import com.goldenratio.leave.ui.activity.MainActivity;
import com.goldenratio.leave.ui.activity.QREncoderActivity;
import com.goldenratio.leave.ui.activity.ScanActivity;
import com.goldenratio.leave.util.AppUtil;
import com.goldenratio.leave.util.GlobalVariable;
import com.goldenratio.leave.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Kiuber on 2016/12/18.
 */

public class LeaveFragment extends Fragment implements View.OnClickListener, MainActivity.backListener {

    public static final int CLASSTEACHERCHECK = 1;
    public static final int DEPARTMENTLEADCHECK = 3;
    public static final int MAXLIMITS = 7;

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
    private TextView mTvDaysNum;
    private EditText mEtWhy;
    private Button mBtnSubmit;
    private EditText etWhy;

    private ProgressDialog mPd;

    private boolean timeFlag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leave, null);

        initView();
        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.ALL);

        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 20);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        return view;
    }

    private String getTime(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
        mTvDaysNum = (TextView) view.findViewById(R.id.et_days);
        mEtWhy = (EditText) view.findViewById(R.id.et_why);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);

        Linkify.addLinks(mTvDaysNum, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(mEtWhy, Linkify.PHONE_NUMBERS);
        mRlType.setOnClickListener(this);
        mRlStartTime.setOnClickListener(this);
        mRlEndTime.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        view.findViewById(R.id.iv_scan).setOnClickListener(this);
        view.findViewById(R.id.iv_encoder).setOnClickListener(this);
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
                        mTvDaysNum.setText(difftime());
                    }
                });
                pvTime.show();               //弹出时间选择器
                break;
            case R.id.rl_end_time:
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        mTvEndTime.setText(getTime(date));
                        mTvDaysNum.setText(difftime());
                        mEtWhy.getText();
                    }
                });
                pvTime.show();
                break;
            case R.id.btn_submit:
                if (AppUtil.isLogin(getContext()) != null) {
                    if (!mTvType.getText().equals(getString(R.string.tv_select)) && timeFlag && !mTvDaysNum.getText().equals("0天0时0分") &&
                            !TextUtils.isEmpty(mEtWhy.getText().toString())) {
                        LeaveBean leaveBean = new LeaveBean();
                        leaveBean.setType(mTvType.getText().toString());
                        leaveBean.setStart(mTvStartTime.getText().toString());
                        leaveBean.setEnd(mTvEndTime.getText().toString());
                        leaveBean.setRemark(mEtWhy.getText().toString());
                        AddNewRecord(leaveBean);
                    } else {
                        showAlertDialog("请检查输入规范后重新提交！");
                    }
                } else {
                    Toast.makeText(getContext(), "请先登录~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_scan:
                startActivity(new Intent(getContext(), ScanActivity.class));
                break;
            case R.id.iv_encoder:
                String status = AppUtil.isLogin(getContext());
                if (status != null) {
                    startActivity(new Intent(getContext(), QREncoderActivity.class));
                } else {
                    Toast.makeText(getContext(), "请先登录~", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }


    //求时间差

    private String difftime() {
        String sTime = mTvStartTime.getText().toString();
        String eTime = mTvEndTime.getText().toString();
        if (sTime.equals(getString(R.string.tv_select)) || eTime.equals(getString(R.string.tv_select))) {
            timeFlag = false;
            return "0天0时0分";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date start = null;
        Date end = null;
        try {
            start = df.parse(sTime);
            end = df.parse(eTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = end.getTime() - start.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day < 0 || hour < 0 || min < 0) {
            showAlertDialog("结束时间小于开始时间，请检查后重新选择！");
            timeFlag = false;
            return "请重新选择";
        }
        StringBuilder strB = new StringBuilder();
        strB.append(day + "天" + hour + "时" + min + "分");
        if (l > 0 && day <= DEPARTMENTLEADCHECK) {
            strB.append(" (需经班主任审核)");
            timeFlag = true;
        } else if (day >= DEPARTMENTLEADCHECK && day <= MAXLIMITS) {
            strB.append(" (需经班主任、导员审核)");
            timeFlag = true;
        } else if (day > MAXLIMITS) {
            strB.append(" (请假时间超过限制)");
            timeFlag = false;
        }
        return strB.toString();
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("取消", null);
        builder.create().show();
    }

    private void showTypeAlertDialog() {
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

    public void AddNewRecord(LeaveBean leaveBean) {
        showProgressDialog();
        String webServiceIp = "http://123.206.23.28/Leave.asmx/";
        if (!(webServiceIp == null)) {
            String url = webServiceIp + "NewRecord";
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("id", SharedPreferenceUtil.getOne(getContext(), GlobalVariable.FILE_NAME_USER_INFO, "id"))
                    .add("start", leaveBean.getStart())
                    .add("end", leaveBean.getEnd())
                    .add("type", leaveBean.getType())
                    .add("remark", leaveBean.getRemark())
                    .add("uuid", SharedPreferenceUtil.getOne(getContext(), GlobalVariable.FILE_NAME_USER_INFO, "uuid"))
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    final String e1 = e.getMessage();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e1, Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String s = parseNewRecordResult(response.body().string());
                        getActivity().runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                recStaus();
                                //   mBtnSubmit.setEnabled(false);
                                //   mBtnSubmit.setText("请耐心等待审核");
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                    }
                    closeProgressDialog();
                }
            });
        } else {
            Toast.makeText(getActivity(), "服务器地址获取失败，请重新试一次~", Toast.LENGTH_SHORT).show();
        }
    }

    private String parseNewRecordResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.getString("code");
            if (code.equals("2001")) {
                return "提交成功~";
            } else {
                return jsonObject.getString("remark");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public boolean callBack() {
        if (pvTime.isShowing()) {
            pvTime.dismiss();
            return true;
        }
        return false;
    }

    private void recStaus() {
        mTvType.setText(R.string.tv_select);
        mTvStartTime.setText(R.string.tv_select);
        mTvStartTime.setText(R.string.tv_select);
        mTvEndTime.setText(R.string.tv_select);
        mTvDaysNum.setText("");
        mEtWhy.setText("");
    }

    private void closeProgressDialog() {
        if (mPd != null && mPd.isShowing()) {
            mPd.dismiss();
            mPd = null;
        }
    }

    private void showProgressDialog() {
        if (mPd == null) {
            mPd = new ProgressDialog(getActivity());
            mPd.setMessage("正在提交");
            mPd.setCancelable(false);
            mPd.show();
        }
    }
}
