package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
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
import com.goldenratio.leave.util.GetDataUtil;
import com.goldenratio.leave.util.GlobalVariable;
import com.goldenratio.leave.util.MD5Util;
import com.goldenratio.leave.util.SharedPreferenceUtil;
import com.goldenratio.leave.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private TextView mTvLogin;
    private ProgressDialog progressDialog;

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

        mTvLogin = (TextView) findViewById(R.id.tv_login);
        mTvLogin.setOnClickListener(this);
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
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在登陆");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String id = mEtId.getText().toString();
                String pwd = mEtPwd.getText().toString();
                if (id.equals("")) {
                    Toast.makeText(this, "请输入学号！", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals("")) {
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    getUserInfo(id, pwd);
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

    private void getUserInfo(String id, String pwd) {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int what = msg.what;
                String obj = msg.obj.toString();
                if (what == 1) {
                    ArrayList<String> loginSuccess = isLoginSuccess(obj);
                    if (loginSuccess.size() == 2) {
                        String s = loginSuccess.get(1);
                        if (TextUtils.equals(s, "登陆成功")) {
                            saveUserInfo(obj);
                            finish();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, loginSuccess.get(1), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, loginSuccess.get(0), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else if (what == 0) {
                    Toast.makeText(LoginActivity.this, obj, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "本地未知错误~", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                return false;
            }
        });
        GetDataUtil.login(id, pwd, handler);
    }

    private ArrayList<String> isLoginSuccess(String json) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.getString("code");
            String remark = jsonObject.getString("remark");
            strings.add(code);
            strings.add(remark);
            return strings;
        } catch (JSONException e) {
            e.printStackTrace();
            strings.add(e.getMessage());
            return strings;
        }
    }

    private void saveUserInfo(String obj) {
        List<ArrayList<String>> arrayLists = decodeUserInfo(obj);
        if (mCbLoginState.isChecked()) {
            // 保持登陆
            // login_state 由登陆状态与id的MD5组成。1为登陆 0为未登陆
            String id = arrayLists.get(1).get(0);
            boolean b = SharedPreferenceUtil.putOne(LoginActivity.this, GlobalVariable.FILE_NAME_APP_CONFIG, "login_state", "1" + MD5Util.createMD5(id));
            boolean user_info = SharedPreferenceUtil.putMultiple(LoginActivity.this, "user_info", arrayLists.get(0), arrayLists.get(1));
            if (b && user_info) {
                Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "信息保存失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        } else {
            //不保持登陆
            boolean user_info = SharedPreferenceUtil.putMultiple(LoginActivity.this, GlobalVariable.FILE_NAME_USER_INFO, arrayLists.get(0), arrayLists.get(1));
            if (user_info) {
                Toast.makeText(this, "登陆成功~", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "信息保存失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<ArrayList<String>> decodeUserInfo(String json) {
        List<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("id");
        strings.add("nickname");
        strings.add("name");
        strings.add("sex");
        strings.add("avatar");
        strings.add("aClass");
        strings.add("autograph");
        arrayLists.add(strings);
        try {
            ArrayList<String> strings1 = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.getString("id");
            String nickname = jsonObject.getString("nickname");
            String name = jsonObject.getString("name");
            String sex = jsonObject.getString("sex");
            String avatar = jsonObject.getString("avatar");
            String aClass = jsonObject.getString("class");
            String autograph = jsonObject.getString("autograph");
            strings1.add(id);
            strings1.add(nickname);
            strings1.add(name);
            strings1.add(sex);
            strings1.add(avatar);
            strings1.add(aClass);
            strings1.add(autograph);
            arrayLists.add(strings1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayLists;
    }
}
