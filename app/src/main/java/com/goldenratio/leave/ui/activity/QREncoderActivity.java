package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.util.StatusBarUtil;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by Kiuber on 2016/12/22.
 */
public class QREncoderActivity extends Activity {

    private ImageView mIvQrEncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_encoder);
        initView();
        String content = getCotent4Net();
        StatusBarUtil.setStatusBarColor(this, true, R.color.colorPrimary, false);
        if (content != null) {
            createChineseQRCode(content);
        } else {
            Toast.makeText(this, "查询失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        mIvQrEncode = (ImageView) findViewById(R.id.iv_qr_encoder);
    }

    private void createChineseQRCode(final String content) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(content, BGAQRCodeUtil.dp2px(QREncoderActivity.this, 300));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    mIvQrEncode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(QREncoderActivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public String getCotent4Net() {
        String ii = "";
        for (int i = 0; i < 20; i++) {
            ii += "张庆宝";
        }
        return ii;
    }
}
