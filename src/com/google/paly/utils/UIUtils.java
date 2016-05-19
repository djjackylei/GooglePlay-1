package com.google.paly.utils;

import com.google.paly.application.MyApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

public class UIUtils {
	
	public static Context getContext(){
		return MyApplication.getContext();
	}
	public static Resources getResource(){
		return getContext().getResources();
	}
	/**获取drawable图片资源*/
	public static Drawable getBitmap(int id){
		return getResource().getDrawable(id);
	}
	/**获取handler*/
	public static Handler getHandler(){
		return MyApplication.getHandler();
	}
	/**
	 *获取strings.xml中的string的item 
	 */
	public static String getString(int id){
		return getResource().getString(id);
	}
	/**获取strings.xml中定义的数据的数组*/
	public static String[] getStringArray(int r_id){
		return getResource().getStringArray(r_id);
	}
	/**获取主线程id*/
	public static int getMThreadId(){
		return MyApplication.getmThreadId();
	}
	/**判断是否是主线程*/
	public static boolean ismThread(){
		return android.os.Process.myTid()==getMThreadId();
	}
	/**使更新ui的操作只运行在主线程*/
	public static void runInMainThread(Runnable runnable){
		if(ismThread()){
			runnable.run();
		}else{
			getHandler().post(runnable);
		}
	}
	/**根据颜色的id获取颜色状态选择器
	 * @return */
	public static ColorStateList getColorStateList(int id){
		return getResource().getColorStateList(id);
	}
	/**根据id获取文件中已经定义的颜色*/
	public static int getColor(int id){
		return getResource().getColor(id);
	}
	/**dp2px*/
	public static int dip2px(int dp) {
		return (int) (getResource().getDisplayMetrics().density*dp+0.5);
	}
	/**px2dp*/
	public static int px2dp(int px) {
		return (int)(px/(getResource().getDisplayMetrics().density)+0.5);
	}
	/**根据id获取drawable*/
	public static Drawable getDrawable(int id) {
		return getResource().getDrawable(id);
	}
	/**View.inflate*/
	public static View inflate(int layout_id){
		return View.inflate(getContext(), layout_id, null);
	}
	/**
	 * 获取dimens中的定义的值
	 * @return
	 */
	public static int getDimens(int id) {
		return getResource().getDimensionPixelSize(id);
	}
	public static void removeCallBack(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}
	public static void postDelayed(Runnable runnable, long i) {
		getHandler().postDelayed(runnable, i);
	}
}
