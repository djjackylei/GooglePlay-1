package com.google.paly.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.SubjectInfo;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class SubjectHolder extends BaseHolder<SubjectInfo> {
	private TextView tv_sub_desc;
	private ImageView image;
	@Override
	public void setData2View() {
		SubjectInfo subjectInfo = getData();
		BitmapUtils bitmapUtils=new BitmapUtils(UIUtils.getContext());
		bitmapUtils.display(image, HttpHelper.URL+"image?name="+subjectInfo.getUrl());
		tv_sub_desc.setText(subjectInfo.getDes());
	}

	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_subject);
		image=(ImageView)view.findViewById(R.id.image);
		tv_sub_desc=(TextView)view.findViewById(R.id.tv_sub_desc);
		return view;
	}

}
