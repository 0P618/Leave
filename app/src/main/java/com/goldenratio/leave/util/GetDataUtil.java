package com.goldenratio.leave.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Kiuber on 2017/1/3.
 */

public class GetDataUtil {

    public static void get(String url
            , ArrayList<String> names, ArrayList<String> values, Handler handler) {
        FormBody.Builder builder = new FormBody.Builder();
        if (names != null && values != null && names.size() == values.size()) {
            for (int i = 0; i < names.size(); i++) {
                builder.add(names.get(i), values.get(i));
            }
            exec(url, builder, handler);
        } else {
            Message message = generateMessage(0, "参数不匹配");
            handler.sendMessage(message);
        }
    }

    private static void exec(String url, FormBody.Builder builder, final Handler handler) {

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendMessage(null);
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    handler.sendMessage(generateMessage(1, response.body().string()));
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ArrayList<String> strings = new ArrayList<String>();
//                            strings.add("start");
//                            strings.add("end");
//                            strings.add("remark");
//                        }
//                    });
                }

//                List<List<String>> decode = JsonUtil.decode(response.body().string(), strings);
//                for (int i = 0; i < decode.size(); i++) {
//                    for (int j = 0; j < decode.get(i).size(); j++) {
//                        Log.d(TAG, "onResponse1: " + decode.get(i).get(j));
//                    }
//                }
            }
        });
    }

    private static Message generateMessage(int what, Object object) {
        Message message = new Message();
        message.what = what;
        message.obj = object;
        return message;
    }
}
