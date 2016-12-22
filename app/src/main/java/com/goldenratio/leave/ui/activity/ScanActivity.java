package com.goldenratio.leave.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.util.StatusBarUtil;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by Kiuber on 2016/12/22.
 */
public class ScanActivity extends Activity implements QRCodeView.Delegate, View.OnClickListener {

    private ZXingView mQRCodeView;
    private TextView mTvResult;
    private LinearLayout mLlScanResult;
    private String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initView();
    }

    private void initView() {
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        findViewById(R.id.tv_scan).setOnClickListener(this);
        mLlScanResult = (LinearLayout) findViewById(R.id.ll_scan_result);
        startScan();
    }

    private void startScan() {
        if (result == null) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (mQRCodeView.getVisibility() == View.GONE) {
            mQRCodeView.setVisibility(View.VISIBLE);
        }
        mQRCodeView.setDelegate(this);
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    private void stopScan() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        StatusBarUtil.setStatusBarColor(this, true, R.color.colorPrimary, false);

        if (mLlScanResult.getVisibility() == View.GONE) {
            mLlScanResult.setVisibility(View.VISIBLE);
        }
        mQRCodeView.setVisibility(View.GONE);
        mQRCodeView.stopCamera();
        mQRCodeView.stopSpot();
        mTvResult.setText(result);
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        this.result = result;
        stopScan();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "打开相机失败，请确保硬件存在并给予调用相机权限！", Toast.LENGTH_SHORT).show();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scan:
                startScan();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限申请结果
        if (requestCode == 10011) {
            for (int index = 0; index < permissions.length; index++) {
                switch (permissions[index]) {
                    case Manifest.permission.CAMERA:
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /**用户已经受权*/
                            startScan();
                        } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                            /**用户拒绝了权限*/
                            Toast.makeText(this, "应用没有拍照权限，请授权！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "应用没有拍照权限，请授权！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }
    }
}
