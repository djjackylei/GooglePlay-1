package com.google.paly.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.AppInfo;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class AppSafeHolder extends BaseHolder<AppInfo> implements
		OnClickListener {

	private ImageView[] imageView;
	private LinearLayout[] linearLayouts;
	private ImageView[] imageIcon;
	private TextView[] textViews;
	private LinearLayout ll_des_root;
	private LayoutParams layoutParams;
	private LinearLayout ll_root;
	private ImageView arrow;
	private BitmapUtils bitmapUtils;
	private boolean isOpen = false;
	private ValueAnimator animator;

	@Override
	public void setData2View() {
		bitmapUtils = new BitmapUtils(UIUtils.getContext());
		AppInfo data = getData();
		for (int i = 0; i < 4; i++) {
			if (i < data.getSafeUrlList().size()) {
				bitmapUtils.display(imageView[i], HttpHelper.URL
						+ "image?name=" + data.getSafeUrlList().get(i));
			} else {
				// 不显示服务器没传图片的imageView
				imageView[i].setVisibility(View.GONE);
			}
		}
		for (int i = 0; i < 4; i++) {
			if (i < data.getSafeDesUrlList().size()) {
				// 显示图片
				linearLayouts[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(imageIcon[i], HttpHelper.URL
						+ "image?name=" + data.getSafeDesUrlList().get(i));
				textViews[i].setText(data.getSafeDesList().get(i));
			} else {
				linearLayouts[i].setVisibility(View.GONE);
			}
		}
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_deatil_appsafe);
		// 顶部safe安全图标 和是否有广告的图标
		imageView = new ImageView[4];
		imageView[0] = (ImageView) view.findViewById(R.id.safe_icon1);
		imageView[1] = (ImageView) view.findViewById(R.id.safe_icon2);
		imageView[2] = (ImageView) view.findViewById(R.id.safe_icon3);
		imageView[3] = (ImageView) view.findViewById(R.id.safe_icon4);

		// 描述的父控件
		linearLayouts = new LinearLayout[4];
		linearLayouts[0] = (LinearLayout) view.findViewById(R.id.des_ll_1);
		linearLayouts[1] = (LinearLayout) view.findViewById(R.id.des_ll_2);
		linearLayouts[2] = (LinearLayout) view.findViewById(R.id.des_ll_3);
		linearLayouts[3] = (LinearLayout) view.findViewById(R.id.des_ll_4);

		imageIcon = new ImageView[4];
		imageIcon[0] = (ImageView) view.findViewById(R.id.des_icon1);
		imageIcon[1] = (ImageView) view.findViewById(R.id.des_icon2);
		imageIcon[2] = (ImageView) view.findViewById(R.id.des_icon3);
		imageIcon[3] = (ImageView) view.findViewById(R.id.des_icon4);

		textViews = new TextView[4];
		textViews[0] = (TextView) view.findViewById(R.id.des_tv1);
		textViews[1] = (TextView) view.findViewById(R.id.des_tv2);
		textViews[2] = (TextView) view.findViewById(R.id.des_tv3);
		textViews[3] = (TextView) view.findViewById(R.id.des_tv4);

		ll_des_root = (LinearLayout) view.findViewById(R.id.ll_des_root);

		// 默认不显示详细的介绍 默认高度为0 介绍默认为0的高度
		layoutParams = ll_des_root.getLayoutParams();
		layoutParams.height = 0;
		ll_des_root.setLayoutParams(layoutParams);

		// ll_parent
		ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
		ll_root.setOnClickListener(this);

		arrow = (ImageView) view.findViewById(R.id.arrow);
		ll_root.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// 显示扩展的des
		expend();
	}

	private void expend() {
		if (isOpen) {
			// 显示底部描述的原始高度
			isOpen = false;
			//从那个位置 扩展到哪个位置 从什么位置缩回到什么位置 开到关
			//注意animator要是同一个 这为什么直接使用静态的 难道是单例??
			animator=ValueAnimator.ofInt(getMeasureHeight(),0);
		//	animator.ofInt(getMeasureHeight(),0);
			//layoutParams.height =getMeasureHeight() ;
		//	ll_des_root.setLayoutParams(layoutParams);
		} else {
			isOpen = true;
			//从那个位置 扩展到哪个位置 从什么位置缩回到什么位置 开到关
			animator=ValueAnimator.ofInt(0,getMeasureHeight());
			
//			layoutParams.height = 0;
//			ll_des_root.setLayoutParams(layoutParams);
		}
		//给动画监听连续可扩展的监听 监听加回调
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				//获取扩展过程中时刻改变的高度值
				layoutParams.height=(Integer) arg0.getAnimatedValue();
				//设置给控件
				ll_des_root.setLayoutParams(layoutParams);
			}
		});
		animator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
			}
			@Override
			public void onAnimationRepeat(Animator arg0) {
			}
			@Override
			public void onAnimationEnd(Animator arg0) {
				//当动画执行完毕时将imageview的下拉图片改变
				if(isOpen){//TODO  注意前景和背景的区别
					arrow.setBackgroundResource(R.drawable.arrow_up);
				}else{
					arrow.setBackgroundResource(R.drawable.arrow_down);
				}
			}
			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});
		
		//TODO  动画要想运行起来一定要指明时常 和启动
		animator.setDuration(800);
		animator.start();
	}

	// 测量ll_des_root的宽高模式 获取其原始高度大小
	private int getMeasureHeight() {
		// 获取精确的宽度值 为什么是精确的宽度值
		int width = ll_des_root.getMeasuredWidth();
		// 设置控件带宽高模式以及大小
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY);
		// 之多
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST);
		// 将宽高带模式大小的模式设置控件
		ll_des_root.measure(widthMeasureSpec, heightMeasureSpec);
		return ll_des_root.getMeasuredHeight();

	}

}
