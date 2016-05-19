package com.google.paly.holder;

import android.view.View;
import android.widget.ImageView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.AppInfo;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class AppPicHolder extends BaseHolder<AppInfo> {
	private ImageView[] imageViews; 
	@Override
	public void setData2View() {
		AppInfo data= getData();
		BitmapUtils bitmapUtils=new BitmapUtils(UIUtils.getContext());
		for (int i = 0; i < 5; i++) {
			if(i<data.getScreenList().size()){
				imageViews[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(imageViews[i], HttpHelper.URL+"image?name="+data.getScreenList().get(i));
			}else{
				imageViews[i].setVisibility(View.GONE);
			}
			
		}
	}

	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_app_pic);
		imageViews=new ImageView[5];
		imageViews[0]=(ImageView) view.findViewById(R.id.imageview1);
		imageViews[1]=(ImageView) view.findViewById(R.id.imageview2);
		imageViews[2]=(ImageView) view.findViewById(R.id.imageview3);
		imageViews[3]=(ImageView) view.findViewById(R.id.imageview4);
		imageViews[4]=(ImageView) view.findViewById(R.id.imageview5);
		return view;
	}

}
