package com.google.paly.ui.widgest;

import com.google.paly.R;
import com.google.paly.utils.UIUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {

	private float ratio;

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initRatio(attrs);
	}
	
	private void initRatio(AttributeSet attrs) {
		//获取自定义属性的第二种方式
		//ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/com.google.paly", "ratio", 0.0f);
		//获取attrs.xml文件中定义的自定义属性
		TypedArray typedArray = UIUtils.getContext().obtainStyledAttributes(attrs,R.styleable.RatioLayout);
		//获取在xml布局文件中
		ratio=typedArray.getFloat(0, 0.0f);
	}
	//重新设置控件下iamgview的宽高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//widthMeasureSpec分为两部分组成：模式，大小
		//32 0,1  前两位为模式   30 大小的值
		//MeasureSpec.AT_MOST     至多的模式     200
//		MeasureSpec.EXACTLY     确切模式         50
//		MeasureSpec.UNSPECIFIED 未定义        
		//当前控件的宽度是固定的 高度是不确定的 
		int width=MeasureSpec.getSize(widthMeasureSpec);
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		
		//如果高度为确切模式的话
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		//计算内部imageView的宽度操作
		int imageWidth=width-getPaddingLeft()-getPaddingRight();
		//根据宽度和比例 获取其高度
		if(ratio!=0.0f&&widthMode==MeasureSpec.EXACTLY&&heightMode!=MeasureSpec.EXACTLY){
			//w:h 2.43:1 w=imageWidth h
			int heightSize=(int)(imageWidth/ratio)+getPaddingBottom()+getPaddingTop();
			
			//将计算出来的自定义控件的高度重新设置为可确定的类型 并赋值给控件
			heightMeasureSpec=MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


}
