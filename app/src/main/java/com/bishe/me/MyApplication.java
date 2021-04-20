package com.bishe.me;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bishe.me.bean.Attraction;
import com.bishe.me.bean.Bill;
import com.bishe.me.bean.Raider;
import com.bishe.me.bean.User;
import com.bishe.me.location.LocationService;
import com.bishe.me.util.MD5;
import com.bishe.me.util.SPUtils;
import com.bishe.me.util.StringUtil;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.LitePalApplication;


public class MyApplication extends LitePalApplication {

    private static final String TAG = "luoxin";
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

        if ((Integer) SPUtils.get(this,"attraction",0) < 1){

            String userJson = StringUtil.getJson("user.json", this);
            try{
                // 整个最大的JSON数组
                JSONArray jsonArray = new JSONArray(userJson);
                Log.d(TAG, "analyzeJSONArray1 jsonArray:" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    // JSON数组里面的具体-JSON对象
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.optString("name", null);
                    String phone = jsonObject.optString("phone", null);
                    int score = jsonObject.optInt("score", 0);
                    User user = new User();
                    user.setScore(score);
                    user.setUserName(name);
                    user.setPhone(phone);
                    user.setPassword(MD5.encryptStr(name));
                    user.save();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            User user = new User();
            user.setUserName("luoxin");
            // 密码MD5加密
            user.setPassword(MD5.encryptStr("luoxin"));
            int score = 0;
            while (score < 700){
                score = new Random().nextInt(900);
            }
            user.setScore(score);
            user.save();

            String json = StringUtil.getJson("attraction.json", this);
            try{
                // 整个最大的JSON数组
                JSONArray jsonArray = new JSONArray(json);
                Log.d(TAG, "analyzeJSONArray1 jsonArray:" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    // JSON数组里面的具体-JSON对象
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.optString("title", null);
                    String publishTime = jsonObject.optString("publishTime", null);
                    String content = jsonObject.optString("content", null);
                    int price = jsonObject.optInt("price", 0);
                    String firstImg = jsonObject.optString("firstImg", null);
                    String imgList = jsonObject.optString("imgList",null);
                    Attraction attraction = new Attraction();
                    attraction.setTitle(title);
                    attraction.setContent(content);
                    attraction.setPublishTime(publishTime);
                    attraction.setPrice(price);
                    attraction.setFirstImg(firstImg);
                    attraction.setImgRes(imgList);
                    attraction.save();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String raiderJson = StringUtil.getJson("raider.json", this);
            try{
                // 整个最大的JSON数组
                JSONArray jsonArray = new JSONArray(raiderJson);
                Log.d(TAG, "analyzeJSONArray1 jsonArray:" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    // JSON数组里面的具体-JSON对象
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.optString("name", null);
                    String time = jsonObject.optString("time", null);
                    String content = jsonObject.optString("content", null);
                    String author = jsonObject.optString("author", null);
                    int status = jsonObject.optInt("status", 0);
                    Raider raider = new Raider();
                    raider.setName(name);
                    raider.setContent(content);
                    raider.setTime(time);
                    raider.setAuthor(author);
                    raider.setStatus(status);
                    raider.save();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String orderJson = StringUtil.getJson("order.json", this);
            try{
                // 整个最大的JSON数组
                JSONArray jsonArray = new JSONArray(orderJson);
                Log.d(TAG, "analyzeJSONArray1 jsonArray:" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    // JSON数组里面的具体-JSON对象
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String orderCode = jsonObject.optString("orderCode", null);
                    String createTime = jsonObject.optString("createTime", null);
                    int attractionID = jsonObject.optInt("attractionID", 0);
                    int num = jsonObject.optInt("num", 0);
                    int status = jsonObject.optInt("status", 0);
                    int authorID = jsonObject.optInt("authorID", 0);
                    Bill order = new Bill();
                    List<User> userList = LitePal.where("id = ?", String.valueOf(authorID)).limit(1)
                        .find(User.class);
                    order.setAuthorID(userList.get(0).getId());
                    order.setOrderCode(orderCode);

                    List<Attraction> attractions = LitePal
                        .where("id=?", String.valueOf(attractionID)).limit(1)
                        .find(Attraction.class);
                    order.setAttractionName(attractions.get(0).getTitle());
                    order.setImg(attractions.get(0).getFirstImg());
                    order.setAttractionID(attractionID);
                    order.setStatus(status);
                    int price = attractions.get(0).getPrice();
                    order.setPrice(price);
                    order.setNum(num);
                    order.setShould(price*num);
                    order.setPaid(price*num);
                    order.setAuthorName(userList.get(0).getUserName());
                    order.setAttractionContent(attractions.get(0).getContent());
                    order.setCreateTime(createTime);
                    order.save();
                    System.out.println("1");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            SPUtils.put(this,"attraction",1);
        }
    }

    /**
     * 功  能：获取当前运行apk的版本号
     * 参  数：context - 上下文环境
     * 返回值：版本名称
     **/
    public static String getVersionName(@NonNull Context context) {
        String result = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            result = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
