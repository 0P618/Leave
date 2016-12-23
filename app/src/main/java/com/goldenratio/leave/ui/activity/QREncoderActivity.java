package com.goldenratio.leave.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldenratio.leave.R;
import com.goldenratio.leave.util.FileUtil;
import com.goldenratio.leave.util.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by Kiuber on 2016/12/22.
 */
public class QREncoderActivity extends Activity implements View.OnClickListener {

    private ImageView mIvQrEncode;
    private Bitmap bitmap = null;
    private TextView mTvSave;


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
        mTvSave = (TextView) findViewById(R.id.tv_save);
        mTvSave.setOnClickListener(this);
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
                    set(bitmap);
                } else {
                    Toast.makeText(QREncoderActivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void set(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCotent4Net() {
        String ii = "";
        for (int i = 0; i < 20; i++) {
            ii += "张庆宝";
        }
        return ii;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                if (bitmap != null) {
                    boolean b = saveImage2Gallery(bitmap);
                    if (b) {
                        mTvSave.setText("保存成功！");
                        mTvSave.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                        mTvSave.setClickable(false);
                    }
                } else {
                    Toast.makeText(this, "保存失败！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean saveImage2Gallery(Bitmap bmp) {
        boolean sdCardMounted = FileUtil.isSDCardMounted();
        if (sdCardMounted) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "image");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                MediaStore.Images.Media.insertImage(getContentResolver()
                        , file.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //通知图库刷新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                    , Uri.parse("file://" + file.getAbsolutePath())));
            return true;
        } else {
            Toast.makeText(this, "保存失败，请检查存储设备是否存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
