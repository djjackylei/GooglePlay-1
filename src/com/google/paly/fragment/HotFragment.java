package com.google.paly.fragment;

import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.paly.base.BaseFragment;
import com.google.paly.protocol.HotProtocol;
import com.google.paly.ui.widgest.FlowLayout;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.utils.DrawableUtils;
import com.google.paly.utils.UIUtils;

/**
 * 排行界面主要使用到一个自定义控件 FlowLayout 和绘制textview的背景图 为圆角矩形 
 * 				使用代码创建颜色选择器 注意点设置状态选择器的控件要有点击事件
 * @author yanbinadmin
 *
 */
public class HotFragment extends BaseFragment {

	private List<String> data;

	@Override
	public View onCreateSuccessView() {
		//观察ui 发现最外层有个scrollView 使用自定义控件FlowLayout 包裹TextView
		ScrollView scrollView=new ScrollView(UIUtils.getContext());
		int padding =UIUtils.dip2px(10);
		scrollView.setPadding(padding, padding, padding, padding);
		
		FlowLayout flowLayout=new FlowLayout(UIUtils.getContext());
		
		//设置每一个TextView的水平的间距
		flowLayout.setHorizontalSpacing(UIUtils.dip2px(6));
		//设置每一个TextView垂直的间距
		flowLayout.setVerticalSpacing(UIUtils.dip2px(10));
		
		for(int i=0;i<data.size();i++){
		final TextView textView=new TextView(UIUtils.getContext());
		textView.setText(data.get(i));
		
		textView.setTextSize(UIUtils.dip2px(15));
		textView.setTextColor(Color.BLACK);
		//字体在控件中居中
		textView.setGravity(Gravity.CENTER); 
		//背景和内部文字的内边距
		flowLayout.setPadding(padding, padding, padding, padding);
		
		//绘制背景图片 圆角矩形带随机颜色
		//准备颜色
		int red = 30+new Random().nextInt(210);
		int green = 30+new Random().nextInt(210);
		int blue = 30+new Random().nextInt(210);
		int rgb = Color.rgb(red, green, blue);
		//片白色
		int pressRgb=0xffcecece;
		//绘制背景图片 添加到状态选择器中
		Drawable drawableNormal=DrawableUtils.getGradientDrawable(rgb,rgb,UIUtils.dip2px(6));
		Drawable drawablePress = DrawableUtils.getGradientDrawable(pressRgb,pressRgb,UIUtils.dip2px(6));
		
		//创建状态选择器
		StateListDrawable stateListDrawable= DrawableUtils.getStateListDrawable(drawableNormal,drawablePress);
		
		textView.setBackgroundDrawable(stateListDrawable);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(UIUtils.getContext(), textView.getText().toString(), 0).show();
			}
		});
		flowLayout.addView(textView);
		}
		scrollView.addView(flowLayout);
		return scrollView;
	}

	@Override
	public ResultState onLoad() {
		HotProtocol hotProtocol = new HotProtocol();
		data = hotProtocol.getData(0);
		return check(data);
	}

}
