package com.google.paly.base;

import android.view.View;

public abstract class BaseHolder<T> {
	private View view;
	private T data;

	protected BaseHolder() {
		view = initView();
		view.setTag(this);
	}

	public void setData(T data) {
		this.data = data;
		setData2View();
	}

	public T getData() {
		return data;
	}
	/**
	 * 返回listview中convertview的对象 也是listview中item显示的对象
	 * @return
	 */
	public View getRootView() {
		return view;
	}

	/**
	 * 将数据设置到控件上
	 */
	public abstract void setData2View();

	/**
	 * xml->view findViewById (创建ViewHolder创建BaseHolder时调用 )
	 * @return listView 中的convertView
	 */
	public abstract View initView();
}