package com.google.paly.utils;

import android.R;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {
	/**
	 * 
	 * @param rgb 绘制图片的颜色
	 * @param stroke 绘制图片边缘的颜色
	 * @param r 绘制圆角矩形内切圆的半径
	 * @return 圆角矩形的背景图片
	 */
	public static GradientDrawable getGradientDrawable(int rgb,int stroke,int r) {
		//创建一个绘制图片的对象
		GradientDrawable gradientDrawable=new GradientDrawable();
		//绘制圆角矩形
		gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
		//绘制图片的颜色
		gradientDrawable.setColor(rgb);
		//设置边缘的颜色
		gradientDrawable.setStroke(2,stroke );
		//设置圆角内切圆的半径
		gradientDrawable.setCornerRadius(r);
		return gradientDrawable;
	}
	/**
	 * 生成状态选择器图片
	 * @param drawableNormal 控件没被点击时的状态
	 * @param drawablePress 控件被点击时的状态
	 * @return
	 */
	public static StateListDrawable getStateListDrawable(Drawable drawableNormal, Drawable drawablePress) {
		StateListDrawable drawable=new StateListDrawable();
							// An array of resource Ids to associate with the image
		drawable.addState(new int[]{R.attr.state_enabled,R.attr.state_pressed}, drawablePress);
		
		//控件可用,但是没有选中,非高亮图片
		drawable.addState(new int[]{R.attr.state_enabled},drawableNormal);
		//控件不可用 非高亮图片
		drawable.addState(new int[]{}, drawableNormal);
		
		return drawable;
	}

}
