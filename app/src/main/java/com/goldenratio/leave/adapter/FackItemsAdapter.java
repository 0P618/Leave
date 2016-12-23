package com.goldenratio.leave.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldenratio.leave.R;
import com.goldenratio.leave.bean.LeaveBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by 冰封承諾Andy on 2016/12/20 0020.
 */

public class FackItemsAdapter extends BaseAdapter {

    private List<LeaveBean> mLeaveList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private boolean isFailure;
    // 柔和、明亮、温柔颜色集合
    private String[] bgColors = {"#FFFFCC", "#CCFFFF", "#FFFF99", "#FF9966", "#FF6666",
            "#FFCCCC", "#CCCCFF", "#CCFFCC", "#CCCCCC", "#CCFF99", "#99CCFF"
            , "#FFFFFF", "#99CC99", "#99CCCC", "#FFCC99", "#CC9999"};
    private ArrayList<String> bgColorList = new ArrayList<>();
    private ArrayList<Integer> randomColorList = new ArrayList<>();

    public FackItemsAdapter(Context context, List<LeaveBean> list, boolean flag) {
        mContext = context;
        mLeaveList = list;
        isFailure = flag;
        mLayoutInflater = LayoutInflater.from(context);
        generateRandomColorList();
    }

    private void generateRandomColorList() {

        for (int i = 0; i < bgColors.length; i++) {
            bgColorList.add(bgColors[i]);
        }

        for (int i = 0; i < mLeaveList.size(); i++) {
            Random random = new Random();
            int j = random.nextInt(bgColorList.size());
            String strColor = bgColorList.get(j);
            Integer color = Color.parseColor(strColor);
            randomColorList.add(color);
            Log.d(TAG, "generateRandomColorList: " + bgColorList.get(i));
            bgColorList.remove(j);
        }
    }

    @Override
    public int getCount() {
        return mLeaveList.size();
    }

    @Override
    public LeaveBean getItem(int i) {
        return mLeaveList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.fack_items, null);
            viewHolder.initView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.initData(position);
        return convertView;
    }

    class ViewHolder {
        private TextView mTvName;
        private TextView mTvTime;
        private TextView mTvType;
        private LinearLayout mLlItem;

        public void initView(View view) {
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
            mTvType = (TextView) view.findViewById(R.id.tv_type);
            mLlItem = (LinearLayout) view.findViewById(R.id.ll_item);
        }

        private void initData(int position) {
            mTvName.setText(getItem(position).getName());
            mTvTime.setText(getItem(position).getStart_time() + " 至 " + getItem(position).getEnd_time());
            mTvType.setText(getItem(position).getType());
            mLlItem.setBackgroundColor(randomColorList.get(position));
        }
    }
}
