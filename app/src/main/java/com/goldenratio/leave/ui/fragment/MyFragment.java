package com.goldenratio.leave.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.goldenratio.leave.R;
import com.goldenratio.leave.ui.activity.LoginActivity;
import com.goldenratio.leave.ui.activity.PersonActivity;
import com.goldenratio.leave.ui.activity.SettingActivity;
import com.goldenratio.leave.util.AppUtil;
import com.goldenratio.leave.util.GlobalConstant;
import com.goldenratio.leave.util.SharedPreferenceUtil;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.goldenratio.leave.util.AppUtil.APP_SETTING;
import static com.goldenratio.leave.util.AppUtil.USER_LOGIN;

/**
 * Created by Kiuber on 2016/12/19.
 */

public class MyFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView mTvLoginState;
    private String id;
    private CircleImageView mCivAvatar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        initView();
        initUserInfo();
        return view;
    }

    private void initView() {
        mTvLoginState = (TextView) view.findViewById(R.id.tv_login_state);
        mCivAvatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        mTvLoginState.setOnClickListener(this);
        view.findViewById(R.id.iv_setting).setOnClickListener(this);
        view.findViewById(R.id.tv_person).setOnClickListener(this);
        view.findViewById(R.id.tv_scan).setOnClickListener(this);
        view.findViewById(R.id.tv_encoder).setOnClickListener(this);
        view.findViewById(R.id.tv_change_pwd).setOnClickListener(this);
        view.findViewById(R.id.tv_help).setOnClickListener(this);
        view.findViewById(R.id.tv_feedback).setOnClickListener(this);
    }

    private void initUserInfo() {
        id = AppUtil.isLogin(getContext());
        if (id != null) {
            // 用户已登陆
            String name = SharedPreferenceUtil.getOne(getContext(), GlobalConstant.FILE_NAME_USER_INFO, "name");
            mTvLoginState.setText(name);
            mTvLoginState.setClickable(false);
            String avatar = SharedPreferenceUtil.getOne(getContext(), GlobalConstant.FILE_NAME_USER_INFO, "avatar");
            if (!avatar.equals("")) {
                Glide.with(getContext()).load(avatar).into(mCivAvatar);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_state:
                startActivityForResult(new Intent(getContext(), LoginActivity.class), USER_LOGIN);
                break;
            case R.id.iv_setting:
                startActivityForResult(new Intent(getContext(), SettingActivity.class), APP_SETTING);
                break;
            case R.id.tv_person:
                if (id != null) {
                    startActivity(new Intent(getContext(), PersonActivity.class));
                } else {
                    Toast.makeText(getContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case USER_LOGIN:
                if (resultCode == RESULT_OK) {
                    // 登陆返回
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
                break;
            case APP_SETTING:
                if (resultCode == RESULT_OK) {
                    // 退出返回
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
                break;
        }
    }
}