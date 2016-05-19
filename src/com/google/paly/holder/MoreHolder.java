package com.google.paly.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.base.MyBaseAdapter;
import com.google.paly.utils.UIUtils;

public class MoreHolder extends BaseHolder<Integer> {
	private TextView load_more_error_tv;
	private LinearLayout load_more_ll;
	private MyBaseAdapter adapter;
	//分析 加载更多的holder有三种状态  每种状态对应一种布局
	//1有数据显示加载更多的view 一个进度条 一个textview
	public static final int LOAD_MORE=0;
	//2没数据了 不显示 
	public static final int NO_MORE=1;
	//3获取数据吹错  加载失败 显示访问网络出错请重试
	public static final int LOAD_ERROR=3;
	//当前状态是根据getData 
	//pirvate static 
	//0
	public MoreHolder(boolean isHasMore,MyBaseAdapter adapter){
		setData(isHasMore?LOAD_MORE:NO_MORE);
		this.adapter=adapter;
	}
	//2
	@Override
	public void setData2View() {
		//根据访问网络之后服务器是否还有数据 返回状态决定
		if(getData()==LOAD_MORE){
			//显示加载更多界面
			load_more_ll.setVisibility(View.VISIBLE);
			load_more_error_tv.setVisibility(View.GONE);
		}else if(getData()==LOAD_ERROR){
			load_more_ll.setVisibility(View.GONE);
			load_more_error_tv.setVisibility(View.VISIBLE);
		}else{
			load_more_ll.setVisibility(View.GONE);
			load_more_error_tv.setVisibility(View.GONE);
		}
	}
	//1
	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_load_more);
		load_more_ll = (LinearLayout) view.findViewById(R.id.load_more_ll);
		load_more_error_tv = (TextView)view.findViewById(R.id.load_more_error_tv);;
		return view;
	}
	
	//TODO  
	@Override
	public View getRootView() {
		//在此处进行网络访问服务器获取更多的数据 
		//当前类知道去哪个网址请求数据吗 不确定 所以去找创建此类对象的类 所以将MyBaseAdapter作为构造函数参数传进来
		if(adapter!=null&&getData()==LOAD_MORE){
			adapter.loadMore();
		}
		return super.getRootView();
	}
}
