package com.goldenratio.leave.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.adapter.HistoryItemAdapter;
import com.goldenratio.leave.bean.LeaveBean;
import com.goldenratio.leave.util.AppUtil;
import com.goldenratio.leave.util.GetDataUtil;

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
    private LinearLayout mLlIng;
    private TextView mTvName;
    private TextView mTvType;
    private TextView mTvTime;
    private ListView mLvHistory;
    private TextView mTvTip;
    private String mStrId = null;

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
        mLlIng = (LinearLayout) view.findViewById(R.id.ll_ing);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvType = (TextView) view.findViewById(R.id.tv_type);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mLvHistory = (ListView) view.findViewById(R.id.lv_history);
        mTvTip = (TextView) view.findViewById(R.id.tv_tip);
        view.findViewById(R.id.iv_help).setOnClickListener(this);
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
                        if (jsonStr != null) {
                            List<LeaveBean> leaveBeenList = json2Bean(jsonStr);
                            if (leaveBeenList != null) {
//                            0审核失败
//                            1审核成功
//                            2正在审核

                                if (leaveBeenList.get(0).getStatus().equals("正在审核")) {
                                    mLlIng.setVisibility(View.VISIBLE);
                                    setContent(leaveBeenList.get(0));
                                    leaveBeenList.remove(0);
                                }

                                mTvTip.setVisibility(View.GONE);
                                historyItemAdapter = new HistoryItemAdapter(getContext(), leaveBeenList);
                                mLvHistory.setAdapter(historyItemAdapter);
                            } else {
                                mTvTip.setText("数据解析失败，请稍后再试！");
                                Toast.makeText(context, "数据解析失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mTvTip.setText("数据获取失败！");
                            Toast.makeText(context, "数据获取失败！", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return false;
            }
        });
        GetDataUtil.get(url, strings, strings1, handler);
    }

    private void setContent(LeaveBean bean) {
        mTvName.setText("姓名");
        mTvType.setText(bean.getType());
        mTvTime.setText(bean.getStart() + "至" + bean.getEnd());
    }

    private List<LeaveBean> json2Bean(String jsonStr) {
        JSONArray jsonArray;
        List<LeaveBean> leaveBeanList = new ArrayList<>();
        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                LeaveBean leaveBean = new LeaveBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                leaveBean.setStart(jsonObject.getString("start"));
                leaveBean.setEnd(jsonObject.getString("end"));
                leaveBean.setStatus(jsonObject.getString("status"));
                leaveBean.setType(jsonObject.getString("type"));
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
        }
    }
}
