package com.bishe.me.util;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	
	public static String  getTimeAll() {
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		
		return str;
		
	}

	public static String getJson(String fileName, Context context) {
		//将json数据变成字符串
		StringBuilder stringBuilder = new StringBuilder();
		try {
			//获取assets资源管理器
			AssetManager assetManager = context.getAssets();
			//通过管理器打开文件并读取
			BufferedReader bf = new BufferedReader(new InputStreamReader(
				assetManager.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	/**
	 * 生成orderCode
	 */
	public static String getOrderCode() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		String key = format.format(date);
		java.util.Random r = new java.util.Random();
		//  两位随机数
		key += String.format("%02d", Math.abs(r.nextInt(99)));
		return key;
	}
}
