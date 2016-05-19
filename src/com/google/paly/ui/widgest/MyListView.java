package com.google.paly.ui.widgest;

import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		init();
		
	}
	//统一ListVIew样式 去掉难看的默认效果
	private void init() {
		setDivider(null);//去掉分割线
		setCacheColorHint(Color.TRANSPARENT);//去掉黑色背景
		setSelector(android.R.color.transparent);//去掉选择时的背景
	}

}
