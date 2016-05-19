package com.google.paly.holder;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class HomeHeaderHolder extends BaseHolder<List<String>> {
	public HomeHeaderHolder(List<String> picList){
		setData(picList);
	}
	private ViewPager pager;
	private List<String> picList;
	private BitmapUtils utils;
	private RelativeLayout rel_layout;
	//用于存储view的内存变量
	private List<View> viewList=new ArrayList<View>();
	@Override
	public void setData2View() {
		picList = getData();
		// 指定轮播图中的小点
		initDot();
		pager.setAdapter(new MyPagerAdapter());
		pager.setCurrentItem(picList.size()*1000);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				int index = arg0%viewList.size();
				for (int i = 0; i < viewList.size(); i++) {
					View view = viewList.get(i);
					if(i==index){
						view.setBackgroundResource(R.drawable.indicator_selected);
					}else{
						view.setBackgroundResource(R.drawable.indicator_normal);
					}
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		new RunnableTask().start();
	}
	class RunnableTask implements Runnable{
		public void start(){
			UIUtils.removeCallBack(this);
			UIUtils.postDelayed(this,2000);
		}
		@Override
		public void run() {
			pager.setCurrentItem(pager.getCurrentItem()+1);
			//把更新界面的方法都放在了UiUtils中了
			//TODO　注意点　所有更新ui的方法都在UIUtils中了　方便管理
			UIUtils.removeCallBack(this);
			//一直执行延时任务
			UIUtils.postDelayed(this,2000);
		}
	}

	private void initDot() {
		// 点的父布局是线性布局 线性布局的父布局是相对布局
		// 指定线性布局宽高规则 居于右
		RelativeLayout.LayoutParams rel_Params = new android.widget.RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
		rel_Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rel_Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rel_layout.addView(linearLayout, rel_Params);
		// ----------------------------------
		
		viewList.clear();
		for (int i = 0; i < picList.size(); i++) {
			// TODO 
			View view=new View(UIUtils.getContext());
			if(i==0){
				view.setBackgroundResource(R.drawable.indicator_selected);
			}else{
				view.setBackgroundResource(R.drawable.indicator_normal);
			}
			// 指定点的宽高规则
			LinearLayout.LayoutParams ll_Params = new LinearLayout.LayoutParams(
					UIUtils.dip2px(6),//TODO 注意点的大小要指定
					UIUtils.dip2px(6));
			ll_Params.setMargins(0, 0, UIUtils.dip2px(6), UIUtils.dip2px(6));
			linearLayout.addView(view,ll_Params);
			viewList.add(view);//将点存入内存 当viewpager翻页时
			//--------------------
		}
	}
	
	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(UIUtils.getContext());
			
			//"image?name="+data.get(position%data.size())
			utils.display(imageView, HttpHelper.URL+"image?name="+picList.get(position%picList.size()));
			//TODO setScaleType 能做屏幕设配
			imageView.setScaleType(ScaleType.FIT_XY);
			// 注意不要少了这步骤哦i
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public View initView() {
		// 以代码创建轮播图
		// 返回轮播图对应的view对象,不能去使用xml布局编写轮播图
		// 所有子控件使用的宽高规则都是父控件指定的
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				UIUtils.getDimens(R.dimen.list_header_height));

		rel_layout = new RelativeLayout(UIUtils.getContext());
		rel_layout.setLayoutParams(layoutParams);

		pager = new ViewPager(UIUtils.getContext());
		// 指定viewpager的框高规则
		RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		pager.setLayoutParams(relParams);
		// 查看ui效果viewpager占整个相对布局

		rel_layout.addView(pager);
		utils = new BitmapUtils(UIUtils.getContext());
		return rel_layout;
	}

}
