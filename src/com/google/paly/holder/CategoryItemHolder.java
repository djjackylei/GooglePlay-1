package com.google.paly.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.CategoryBean;
import com.google.paly.http.HttpHelper;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class CategoryItemHolder extends BaseHolder<CategoryBean> {

	private ImageView iv_category_item1;
	private TextView tv_category_item1;
	private ImageView iv_category_item2;
	private TextView tv_category_item2;
	private ImageView iv_category_item3;
	private TextView tv_category_item3;

	@Override
	public void setData2View() {
		BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());
		bitmapUtils.display(iv_category_item1, HttpHelper.URL + "image?name="
				+ getData().url1);
		tv_category_item1.setText(getData().getName1());

		bitmapUtils.display(iv_category_item2, HttpHelper.URL + "image?name="
				+ getData().url2);
		tv_category_item2.setText(getData().getName2());
		
		bitmapUtils.display(iv_category_item3, HttpHelper.URL + "image?name="
				+ getData().url3);
		tv_category_item3.setText(getData().getName3());
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_category_item);
		iv_category_item1 = (ImageView) view.findViewById(R.id.iv_category_item1);
		tv_category_item1 = (TextView) view.findViewById(R.id.tv_category_item1);
		
		
		iv_category_item2 = (ImageView) view.findViewById(R.id.iv_category_item2);
		tv_category_item2 = (TextView) view.findViewById(R.id.tv_category_item2);
		
		
		iv_category_item3 = (ImageView) view.findViewById(R.id.iv_category_item3);
		tv_category_item3 = (TextView) view.findViewById(R.id.tv_category_item3);
		
		
		
		
		return view;
	}

}
