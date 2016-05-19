package com.google.paly.base;

import java.util.List;

import com.google.paly.bean.AppInfo;
import com.google.paly.ui.widgest.LoadingPage;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.utils.UIUtils;

import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	private LoadingPage loadingPage;
	public View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	//此方法返回的结果就是viewpager要展示的内容
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadingPage = new LoadingPage(UIUtils.getContext()) {
			@Override
			//当前类又不知道请求网络怎么请求 请求哪个地址 请求结果什么的 所以啊 继续抽象 回调 让子类去实现
			public ResultState onLoad() {
				return BaseFragment.this.onLoad();
			}
			@Override
			public View onCreateSuccessView() {
				//当前类也不知道请求成功后界面长什么样啊 继续抽象 回调 让子类去实现
				return BaseFragment.this.onCreateSuccessView();
			}
		};
		return loadingPage;
	}
	/** 根据请求网络的结果返回请求结果状态 */
	public abstract ResultState onLoad();
	/** 返回请求成功界面应该如何展示的view */
	public abstract View onCreateSuccessView();
	/**请求网络获取数据后显示结果页面调用*/
	public void show(){
		if(loadingPage!=null){
			//子界面请求网络触发方法
			loadingPage.show();
		}
	}
	/**
	 * 根据请求网络获取的数据 返回请求状态 交给loadingpage显示不同状态下的fragment显示的view
	 * @param list2
	 * @return
	 */
	public ResultState check(List list2) {
		if(list2!=null){
			if(list2.size()>0){
				return ResultState.REQUEST_SUCCESS;
			}else{
				return ResultState.REQUEST_EMPTY;
			}
		}else{
			return ResultState.REQUEST_ERROR;
		}
	}
}
