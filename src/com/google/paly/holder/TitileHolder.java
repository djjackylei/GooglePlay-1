package com.google.paly.holder;

import android.view.View;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.CategoryBean;
import com.google.paly.utils.UIUtils;

public class TitileHolder extends BaseHolder<CategoryBean> {

	private TextView category_title_tv;

	public TitileHolder(CategoryBean categoryBean) {
		setData(categoryBean);
	}

	@Override
	public void setData2View() {
		if(getData()!=null){
			category_title_tv.setText(getData().title);
		}
	}

	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_category_title);
		category_title_tv = (TextView)view.findViewById(R.id.category_title_tv);
		return view;
	}

}
