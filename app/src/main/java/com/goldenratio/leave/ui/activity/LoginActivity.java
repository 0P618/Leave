package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.util.MD5Util;
import com.goldenratio.leave.util.SharedPreferenceUtil;
import com.goldenratio.leave.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * Created by Kiuber on 2016/12/20.
 */
public class LoginActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText mEtId;
    private EditText mEtPwd;
    private TextView mTvLine1;
    private TextView mTvLine2;
    private ImageView mIvClear1;
    private ImageView mIvClear2;
    private ImageView mIvPwdState;
    private CheckBox mCbLoginState;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        StatusBarUtil.setStatusBarColor(this, true, R.color.layout_default_bg_color, true);
    }


    private void initView() {
        findViewById(R.id.iv_close).setOnClickListener(this);

        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.tv_question).setOnClickListener(this);

        mIvClear1 = (ImageView) findViewById(R.id.iv_clear_1);
        mIvClear1.setOnClickListener(this);
        mIvClear2 = (ImageView) findViewById(R.id.iv_clear_2);
        mIvClear2.setOnClickListener(this);
        mEtId = (EditText) findViewById(R.id.et_id);
        mEtId.setOnFocusChangeListener(this);
        mEtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEtId.getText().toString().equals("") && mEtId.isFocusable()) {
                    mIvClear1.setVisibility(View.VISIBLE);
                } else {
                    mIvClear1.setVisibility(View.INVISIBLE);
                }
            }
        });
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtPwd.setOnFocusChangeListener(this);
        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEtPwd.getText().toString().equals("") && mEtPwd.isFocusable()) {
                    mIvClear2.setVisibility(View.VISIBLE);
                } else {
                    mIvClear2.setVisibility(View.INVISIBLE);
                }
            }
        });
        mTvLine1 = (TextView) findViewById(R.id.tv_line_1);
        mTvLine2 = (TextView) findViewById(R.id.tv_line_2);
        mIvPwdState = (ImageView) findViewById(R.id.iv_pwd_state);
        mIvPwdState.setOnClickListener(this);
        mCbLoginState = (CheckBox) findViewById(R.id.cb_login_state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_login:
                String id = mEtId.getText().toString();
                String pwd = mEtPwd.getText().toString();
                if (id.equals("")) {
                    Toast.makeText(this, "请输入学号！", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals("")) {
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    String userInfo = getUserInfo();
                    if (userInfo == null) {
                        Toast.makeText(this, "学号或者密码错误！", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<String> keys = new ArrayList<>();
                        keys.add("id");
                        keys.add("nickname");
                        keys.add("sex");
                        keys.add("autograph");
                        ArrayList<String> values = new ArrayList<>();
                        values.add(id);
                        values.add("Kiuber");
                        values.add("男");
                        values.add("革命尚未成功！");
                        if (mCbLoginState.isChecked()) {
                            // 保持登陆
                            // login_state 由登陆状态与id的MD5组成。1为登陆 0为未登陆
                            boolean b = SharedPreferenceUtil.putOne(LoginActivity.this, "app_config", "login_state", "1" + MD5Util.createMD5(id));
                            boolean user_info = SharedPreferenceUtil.putMultiple(LoginActivity.this, "user_info", keys, values);
                            if (b && user_info) {
                                Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(this, "信息保存失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //不保持登陆
                            boolean user_info = SharedPreferenceUtil.putMultiple(LoginActivity.this, "user_info", keys, values);
                            if (user_info) {
                            } else {
                                Toast.makeText(this, "信息保存失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
                break;
            case R.id.iv_clear_1:
                mEtId.setText("");
                break;
            case R.id.iv_clear_2:
                mEtPwd.setText("");
                break;
            case R.id.tv_question:
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("忘记密码请联系系主任");
                builder.show();
                break;
            case R.id.iv_pwd_state:
                if (mEtPwd.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvPwdState.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                } else {
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvPwdState.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                }
                mEtPwd.setSelection(mEtPwd.getText().length());
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_id:
                if (hasFocus) {
                    mTvLine1.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    mTvLine2.setBackgroundColor(getResources().getColor(R.color.gainsboro));
                    mIvClear2.setVisibility(View.INVISIBLE);
                    if (!mEtId.getText().toString().equals("")) {
                        mIvClear1.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.et_pwd:
                if (hasFocus) {
                    mTvLine1.setBackgroundColor(getResources().getColor(R.color.gainsboro));
                    mTvLine2.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    mIvClear1.setVisibility(View.INVISIBLE);
                    if (!mEtPwd.getText().toString().equals("")) {
                        mIvClear2.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private String getUserInfo() {
        return "1513640218";
    }
}
