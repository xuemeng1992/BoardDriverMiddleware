package com.rocktech.boarddriver.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author kaitao.yan
 * @createdate 2013-12-15 下午1:18:57
 * @Description: activity 核心工具类
 */
public class ActivityUtil {
	
	public static ArrayList<Activity> ALL_ACTIVITY = new ArrayList<>();
	
	/**
	 * @Description: 添加Activity到列表中
	 * @param activity
	 */
	public static void addActivityToList(Activity activity){
		if(!ALL_ACTIVITY.contains(activity)){
			ALL_ACTIVITY.add(activity);
		}
	}
	
	/**
	 * @Description: 从列表移除Activity
	 * @param activity
	 */
	public static void removeActivityFromList(Activity activity){
		if(ALL_ACTIVITY.contains(activity)){
			ALL_ACTIVITY.remove(activity);
		}
	}

	public static void finishAllActivity(){
		for (Activity acty : ALL_ACTIVITY) {
			if (acty != null && !acty.isFinishing()){
				acty.finish();
			}
		}
		ALL_ACTIVITY.clear(); //清空所有的activity
	}

	/**
	 * 结束某个Activity
	 * @param classObj
	 * @param <E>
     */
	public static <E extends Activity> void finishActivity(Class<E>... classObj){
		for (Class<E> obj : classObj) {
			for (Activity acty : ALL_ACTIVITY) {
				if (acty != null && acty.getClass() == obj && !acty.isFinishing()){
					acty.finish();
					break;
				}
			}
		}
	}

	/**
	 * 开起新的Activity
	 * @param context
	 * @param cls
     */
	public static void startActivity(Activity context, Class<?> cls){
		Intent intent = new Intent();
		intent.setClass(context,cls);
		context.startActivity(intent);
	}

	/**
	 * 开启新的Activity
	 */
	public static void startActivity(Activity context, Class<?> cls, String paramName, boolean value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivity(intent);
	}

	/**
	 * 开启新的Activity
	 */
	public static void startActivity(Activity context, Class<?> cls, String paramName, String value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivity(intent);
	}

	/**
	 * 开启新的Activity
	 */
	public static void startActivity(Activity context, Class<?> cls, String paramName, int value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivity(intent);
	}

	/**
	 * 开启新的Activity
	 */
	public static void startActivity(Activity context, Class<?> cls, String paramName, Parcelable value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivity(intent);
	}

	/**
	 * 开启新的Activity
	 */
	public static void startActivity(Activity context, Class<?> cls, String paramName, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, bundle);
		context.startActivity(intent);
	}

	/**
	 * 开启新的Activity
	 *
	 */
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode){
		Intent intent = new Intent();
		intent.setClass(context,cls);
		context.startActivityForResult(intent,requestCode);
	}

	/**
	 * 开启新的Activity
	 *
	 */
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, Parcelable value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 开启新的Activity
	 *
	 */
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, boolean value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 开启新的Activity
	 *
	 */
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, int value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 开启新的Activity
	 *
	 */
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, String value) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, value);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 开启新的Activity
	 *
	 */
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtra(paramName, bundle);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 关闭所有的activity
	 * finishActivityList:(这里用一句话描述这个方法的作用)
	 * @date: 2016年8月26日 上午10:34:49
	 * @author kaitao.yan
	 */
	public static void exitApp(){
		if(ALL_ACTIVITY != null){
			for (Activity activity : ALL_ACTIVITY) {
				activity.finish();
			}
			ALL_ACTIVITY.clear();
			//杀死进程
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
