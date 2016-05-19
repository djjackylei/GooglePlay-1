package com.google.paly.holder;

import android.R.anim;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.AppInfo;
import com.google.paly.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class AppdescHolder extends BaseHolder<AppInfo> implements
		OnClickListener {

	private TextView tv_app_desc;
	private TextView tv_app_author;
	private ImageView arrow;
	private AppInfo data;
	private boolean isShow = false;
	private ValueAnimator animator;
	private LinearLayout.LayoutParams layoutParams;
	private LinearLayout ll_root;
	private ScrollView scrollView;

	@Override
	public void setData2View() {
		data = getData();
		tv_app_desc.setText(data.getDes());
		tv_app_author.setText(data.getAuthor());

		// 获取desc的宽高规则

		layoutParams = (LinearLayout.LayoutParams) tv_app_desc
				.getLayoutParams();
		// TODO 此处代码规则居然没拿到值 问题居然是模拟的TextView没设置宽高规则
		layoutParams.height = getShortHeight();
		// 设置默认显示7行 注意点 如果没有7行还要显示7行吗？！
		tv_app_desc.setLayoutParams(layoutParams);
		//
		
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_appdesc);
		//包裹所有des信息的根节点 //将scorllView滚动起来 TODO　找到最外层scrollview对象
		
		ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
		
		//TODO  不能在此执行 此时当前线性布局还没有加入到外层view中
	//	scrollView = getScrollView();
		
		tv_app_desc = (TextView) view.findViewById(R.id.tv_app_desc);
		tv_app_author = (TextView) view.findViewById(R.id.tv_app_author);
		arrow = (ImageView) view.findViewById(R.id.arrow);
		// tv_app_desc
		tv_app_desc.setOnClickListener(this);
		return view;
	}
	
	private ScrollView getScrollView() {
		//找到当前最跟上的线性布局父控件
		View parent = (View) ll_root.getParent();
		while(!(parent instanceof ScrollView)){
			parent=(View) parent.getParent();
			//找到最外层scrollview
		}
		return (ScrollView) parent;
		
	}

	// 获取7行的文字高度
	// TODO 注意点 注意data在setdata2view之后才能被赋值 所以此方法应使用在setData2View方法中
	public int getShortHeight() {
		// 进行长短的判断
		// 获取其宽度的widthMeasureSpec 规则
		int measuredWidth = tv_app_desc.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth,
				MeasureSpec.EXACTLY);
		// 获取其高度的heightMeasureSpec
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST);

		// 模拟一个7行的文字高度
		tv_app_desc.measure(widthMeasureSpec, heightMeasureSpec);
		// 定义一个7行的文字高度
		TextView mtextView = new TextView(UIUtils.getContext());
		mtextView.setLines(7);
		mtextView.setMaxLines(7);
		mtextView.setText(data.getDes());
		mtextView.measure(widthMeasureSpec, heightMeasureSpec);
		// mtextView.getMeasuredHeight();
		return mtextView.getMeasuredHeight();

	}

	// 获取=全部简介的高度
	public int getLongHeight() {
		int measuredWidth = tv_app_desc.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth,
				MeasureSpec.EXACTLY);
		// 获取其高度的heightMeasureSpec
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST);
		tv_app_desc.measure(widthMeasureSpec, heightMeasureSpec);
		return tv_app_desc.getMeasuredHeight();
	}

	@Override
	public void onClick(View v) {
		expend();
	}

	private void expend() {
		int longHeight = getLongHeight();
		int shortHeight = getShortHeight();
		if (shortHeight < longHeight) {
			if (isShow) {
				// 点击后关闭 大-》小
				isShow = !isShow;
				// 如果总的介绍根本没7行就没必要在执行动画
				// if(shortHeight<longHeight) //两个地方都要就放在最外面
				// zhuyi ValueAnimator.ofInt此方法返回的是同一个ValueAnimator
				animator = ValueAnimator.ofInt(longHeight, shortHeight);
			} else {
				// 点击后展开 小 ->大 时
				isShow = !isShow;
				// if(shortHeight<longHeight)
				animator = ValueAnimator.ofInt(shortHeight, longHeight);
			
			}
		}
		if (animator != null) {// 容错处理
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator arg0) {
					// 移动过程中高度值是一点一点的改变所有要将移动过程中的高度值取出来
					layoutParams.height = (Integer) arg0.getAnimatedValue();
					tv_app_desc.setLayoutParams(layoutParams);
				}
			});

			// 注意动画一定要指定执行时间和start方法
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator arg0) {

				}

				@Override
				public void onAnimationRepeat(Animator arg0) {

				}

				@Override
				public void onAnimationEnd(Animator arg0) {
					// 动画执行接收将imageview图片切换
					if (isShow) {
						//小-》大
						 // TODO 将ScrollView触底滚动
						scrollView = getScrollView();
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
						
						arrow.setBackgroundResource(R.drawable.arrow_up);
					} else {
						arrow.setBackgroundResource(R.drawable.arrow_down);
						
					}
					
				}

				@Override
				public void onAnimationCancel(Animator arg0) {

				}
			});
			animator.setDuration(800);
			// 执行动画
			animator.start();
		}
	}

}
