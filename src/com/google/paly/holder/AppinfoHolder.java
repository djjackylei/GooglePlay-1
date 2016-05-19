package com.google.paly.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.AppInfo;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class AppinfoHolder extends BaseHolder<AppInfo> {
	private TextView tv_appname,tv_download,tv_version,tv_apptime,tv_size;
	private ImageView iv_appicon;
	private RatingBar rb_star;
	@Override
	public void setData2View() {
		AppInfo data = getData();
		tv_appname.setText(data.getName());
		tv_download.setText(data.getDownloadNum());
		tv_version.setText(data.getVersion());
		tv_apptime.setText(data.getDate());
		tv_size.setText(Formatter.formatFileSize(UIUtils.getContext(), data.getSize()));
		BitmapUtils bitmapUtils=new BitmapUtils(UIUtils.getContext());
		bitmapUtils.display(iv_appicon, HttpHelper.URL+"image?name="+data.getIconUrl());
		rb_star.setRating(data.getStars());
		
	}

	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_detail_appinfo);
		iv_appicon=	(ImageView) view.findViewById(R.id.iv_appicon);
		tv_appname=(TextView) view.findViewById(R.id.tv_appname);
		
		iv_appicon=(ImageView) view.findViewById(R.id.iv_appicon);
		rb_star=(RatingBar) view.findViewById(R.id.rb_app_star);
		
		tv_download=(TextView) view.findViewById(R.id.tv_download);
		tv_version=(TextView) view.findViewById(R.id.tv_version);
		
		tv_apptime=(TextView) view.findViewById(R.id.tv_apptime);
		tv_size=(TextView) view.findViewById(R.id.tv_size);
		return view;
	}

}
