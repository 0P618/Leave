package com.goldenratio.leave.ui.fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.adapter.HistoryItemAdapter;
import com.goldenratio.leave.bean.LeaveBean;
import com.goldenratio.leave.dao.RecordDao;
import com.goldenratio.leave.util.AppUtil;
import com.goldenratio.leave.util.GetDataUtil;
import com.goldenratio.leave.util.GlobalConstant;
import com.goldenratio.leave.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiuber on 2016/12/18.
 */

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private View view;
    private HistoryItemAdapter historyItemAdapter;
    private ListView mLvHistory;
    private TextView mTvTip;
    private String mStrId = null;
    private ProgressDialog progressDialog;
    private RelativeLayout mRlIng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, null);
        initView();
        mStrId = AppUtil.isLogin(getContext());
        if (mStrId != null) {
            initData();
        } else {
            mTvTip.setText("请先登录~");
        }
        return view;
    }


    private void initView() {
        mRlIng = (RelativeLayout) view.findViewById(R.id.rl_ing);
        mLvHistory = (ListView) view.findViewById(R.id.lv_history);
        mTvTip = (TextView) view.findViewById(R.id.tv_tip);
        view.findViewById(R.id.iv_help).setOnClickListener(this);
        view.findViewById(R.id.iv_refresh).setOnClickListener(this);
    }

    private void initData() {
        String url = "http://123.206.23.28/Leave.asmx/QueryRecord";
        ArrayList<String> strings = new ArrayList<>();
        strings.add("id");
        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add(mStrId);

        final Context context = getContext();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(context, "fail" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        String jsonStr = msg.obj.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            if (jsonObject.getString("code").equals("2001")) {
                                List<LeaveBean> result = json2Bean(jsonObject.getString("result"));
                                if (result != null) {
//                            0审核失败
//                            1审核成功
//                            2正在审核
                                    if (result.get(0).getStatus().equals("正在审核")) {
                                        mRlIng.setVisibility(View.VISIBLE);
                                        setContent(result.get(0));
                                        result.remove(0);
                                    } else {
                                        mRlIng.setVisibility(View.GONE);
                                    }

                                    mTvTip.setVisibility(View.GONE);
                                    historyItemAdapter = new HistoryItemAdapter(getContext(), result);
                                    mLvHistory.setAdapter(historyItemAdapter);

                                    String sql = "SELECT * FROM record";
                                    int a = result.size();
                                    int b = queryBySqlite(sql).getCount();
                                    Log.i("lxt", "handleMessage: " + a + "----" + b);
                                    if (a > b) {
                                        List<LeaveBean> list = new ArrayList<>();
                                        for (int i = 0; i < a - b; i++) {
                                            list.add(result.get(i));
                                        }
                                        insert2Sqlite(list);
                                        Log.i("lxt", "handleMessage: 存储本地数据" + jsonStr);
                                    }
                                } else {
                                    mTvTip.setText("数据解析失败，请稍后再试！");
                                }
                            } else {
                                mTvTip.setText(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mTvTip.setText(e.getMessage());
                        }
                        break;
                }
                if (progressDialog != null)

                {
                    progressDialog.dismiss();
                }
                return false;
            }
        });
        GetDataUtil.get(url, strings, strings1, handler);
    }

    private void setContent(LeaveBean bean) {
        TextView mTvName = (TextView) mRlIng.findViewById(R.id.tv_name);
        TextView mTvTime = (TextView) mRlIng.findViewById(R.id.tv_time);
        TextView mTvType = (TextView) mRlIng.findViewById(R.id.tv_type);
        TextView mTvStatus = (TextView) mRlIng.findViewById(R.id.tv_status);
        TextView mTvRemark = (TextView) mRlIng.findViewById(R.id.tv_remark);
        TextView mTvCreated = (TextView) mRlIng.findViewById(R.id.tv_created);

        mTvName.setText(SharedPreferenceUtil.getOne(getContext(), GlobalConstant.FILE_NAME_USER_INFO, "name"));
        mTvTime.setText(bean.getStart() + " 至 " + bean.getEnd());
        mTvType.setText(bean.getType());
        mTvStatus.setText(bean.getStatus());
        mTvRemark.setText(bean.getRemark());
        mTvCreated.setText(bean.getCreated());
    }

    private List<LeaveBean> json2Bean(String jsonStr) {
        JSONArray jsonArray;
        List<LeaveBean> leaveBeanList = new ArrayList<>();
        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                LeaveBean leaveBean = new LeaveBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                leaveBean.setId(jsonObject.getString("id"));
                leaveBean.setStart(jsonObject.getString("start"));
                leaveBean.setEnd(jsonObject.getString("end"));
                leaveBean.setStatus(jsonObject.getString("status"));
                leaveBean.setRemark(jsonObject.getString("remark"));
                leaveBean.setType(jsonObject.getString("type"));
                leaveBean.setCreated(jsonObject.getString("created"));
                leaveBeanList.add(leaveBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (leaveBeanList.size() != 0) {
            return leaveBeanList;
        } else {
            return null;
        }
    }

    private void insert2Sqlite(List<LeaveBean> list) {
        ContentValues values = new ContentValues();
        RecordDao recordDao = new RecordDao(getActivity());
        for (LeaveBean leaveBean : list) {
            values.put("id", leaveBean.getId());
            values.put("start", leaveBean.getStart());
            values.put("end", leaveBean.getEnd());
            values.put("remark", leaveBean.getRemark());
            values.put("type", leaveBean.getType());
            values.put("status", leaveBean.getStatus());
            values.put("created", leaveBean.getCreated());
            recordDao.insertAllData(values);
            values.clear();
        }
    }

    private Cursor queryBySqlite(String sql) {
        return new RecordDao(getActivity()).queryData(sql, null);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("帮助");
                builder.setMessage("正在审核：在假条开始时间之前班主任正在审核\n\n审核失败：在假条开始时间之前班主任审核失败\n\n审核通过：在假条开始时间之前班主任审核通过\n\n未审核：在假条开始时间之前班主任未进行审核");
                builder.setPositiveButton("我知道了", null);
                builder.show();
                break;
            case R.id.iv_refresh:
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("正在刷新~");
                progressDialog.show();
                initData();
                break;
        }
    }
}
