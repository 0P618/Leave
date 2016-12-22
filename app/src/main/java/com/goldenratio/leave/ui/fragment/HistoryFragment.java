package com.goldenratio.leave.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.adapter.FackItemsAdapter;
import com.goldenratio.leave.bean.LeaveBean;
import com.goldenratio.leave.ui.activity.GetLeaveActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiuber on 2016/12/18.
 */

public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView myFack;
    private FackItemsAdapter mNowAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        myFack = (ListView) view.findViewById(R.id.myFack);

        myFack.setEmptyView(view.findViewById(R.id.nullText));
        myFack.setOnItemClickListener(this);
    }

    private void initData() {
        //TODO 测试数据
        List<LeaveBean> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LeaveBean leave = new LeaveBean();
            leave.setClassName("ruan jian");
            leave.setName("Loli" + i);
            leave.setRemark("这是详细内容");
            leave.setType("病假");
            leave.setStart_time("2016-12-21 11:11:11");
            leave.setEnd_time("2016-12-25 11:11:11");
            list.add(leave);
        }
        if (isEndTime(list.get(0).getEnd_time())) {
            mNowAdapter = new FackItemsAdapter(getActivity(), list, true);
        } else {
            mNowAdapter = new FackItemsAdapter(getActivity(), list, false);
        }

        myFack.setAdapter(mNowAdapter);
    }

    /**
     * 区分假条是否已经过期
     *
     * @param time 时间字符串
     * @return 是否过期
     */
    private boolean isEndTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long EndTime = 0;
        long nowTime = SystemClock.currentThreadTimeMillis();
        try {
            EndTime = sdf.parse(time).getTime();
            if (EndTime >= nowTime) {
                //未过期
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "获取数据错误", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LeaveBean leave = (LeaveBean) parent.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), GetLeaveActivity.class);
        intent.putExtra("name", leave.getName());
        intent.putExtra("className", leave.getClassName());
        intent.putExtra("Stime", leave.getStart_time());
        intent.putExtra("Etime", leave.getEnd_time());
        intent.putExtra("context", leave.getRemark());
        startActivity(intent);
    }
}
