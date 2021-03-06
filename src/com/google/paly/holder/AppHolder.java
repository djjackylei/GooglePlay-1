package com.google.paly.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.AppInfo;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class AppHolder extends BaseHolder<AppInfo> {
	private ImageView app_icon;
	private TextView app_des;
	private TextView app_size;
	private RatingBar rating_bar;
	private TextView app_name;
	private LinearLayout ll_download;
	@Override
	public void setData2View() {
		AppInfo appInfo=getData();
		BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());
										//HttpHelper.URL+"image?name="+picList.get(position%picList.size())
		bitmapUtils.display(app_icon, HttpHelper.URL+"image?name="+appInfo.getIconUrl());
		app_des.setText(appInfo.getDes());
		//可以给size进行下格式化
		String size = Formatter.formatFileSize(UIUtils.getContext(), appInfo.getSize());
		app_size.setText(size);
		rating_bar.setRating(appInfo.getStars());
		app_name.setText(appInfo.getName());
	}

	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_home_item);
		app_icon = (ImageView) view.findViewById(R.id.app_icon);
		app_name=(TextView)view.findViewById(R.id.app_name);
		//显示星星的控件
		rating_bar=(RatingBar)view.findViewById(R.id.rating_bar);
		app_size=(TextView)view.findViewById(R.id.app_size);
		app_des=(TextView)view.findViewById(R.id.app_des);
		
		ll_download=(LinearLayout)view.findViewById(R.id.ll_download);
		//TODO 此界面也设置下下载的逻辑
	//	ll_download.setVisibility(View.GONE);
		return view;
	}
}
