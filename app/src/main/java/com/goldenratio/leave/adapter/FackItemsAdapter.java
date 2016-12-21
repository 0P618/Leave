package com.goldenratio.leave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldenratio.leave.R;
import com.goldenratio.leave.bean.LeaveBean;

import java.util.List;

/**
 * Created by 冰封承諾Andy on 2016/12/20 0020.
 */

public class FackItemsAdapter extends BaseAdapter {

    private List<LeaveBean> mLeaveList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private boolean isNow;

    public FackItemsAdapter(Context context,List<LeaveBean> list,boolean flag){
        mContext = context;
        mLeaveList = list;
        isNow = flag;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mLeaveList.size();
    }

    @Override
    public Object getItem(int i) {
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

    class ViewHolder{
        private TextView mName;
        private TextView mStartTime;
        private TextView mEndTime;
        private TextView mType;

        public void initView(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mStartTime = (TextView) view.findViewById(R.id.startTime);
            mEndTime = (TextView) view.findViewById(R.id.endTime);
            mType = (TextView) view.findViewById(R.id.type);

            if (!isNow){
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.lineItems);
                layout.setBackgroundResource(R.drawable.fack_not_items_bg);
            }
        }

        public void initData(int position) {
            mName.setText("姓名：" + mLeaveList.get(position).getName());
            mStartTime.setText("开始时间：" + mLeaveList.get(position).getStart_time());
            mEndTime.setText("结束时间：" + mLeaveList.get(position).getEnd_time());
            mType.setText("类型：" + mLeaveList.get(position).getType());
        }
    }
}
