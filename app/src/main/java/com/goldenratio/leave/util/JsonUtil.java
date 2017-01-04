package com.goldenratio.leave.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiuber on 2017/1/3.
 */

public class JsonUtil {
    public static List<List<String>> decode(String jsonStr, ArrayList<String> keys) {
        JSONArray jsonArray;
        List<List<String>> result = new ArrayList<>();
        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                List<String> result1 = new ArrayList<>();
                for (int j = 0; j < keys.size(); j++) {
                    String key = keys.get(i);
                    result1.add(jsonObject.getString(key));
                }
                result.add(result1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result;
        } else {
            return null;
        }
    }
}
