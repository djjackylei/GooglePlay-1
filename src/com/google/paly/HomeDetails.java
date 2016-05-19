package com.google.paly;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.google.paly.base.BaseActivity;
import com.google.paly.bean.AppInfo;
import com.google.paly.holder.AppPicHolder;
import com.google.paly.holder.AppSafeHolder;
import com.google.paly.holder.AppdescHolder;
import com.google.paly.holder.AppinfoHolder;
import com.google.paly.holder.DownloadHolder;
import com.google.paly.protocol.HomeDetailsProtocol;
import com.google.paly.ui.widgest.LoadingPage;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.utils.UIUtils;

public class HomeDetails extends BaseActivity {
	private FrameLayout fl_deatil_desc,fl_detail_appinfo,fl_detail_safe;
	private HorizontalScrollView hsv_detail_apppic;
	private AppInfo data;
	private FrameLayout fl_download;
	
	private ActionBar actionBar;
	private DownloadHolder downloadHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadingPage view=new LoadingPage(this) {
			@Override
			public ResultState onLoad() {
				return HomeDetails.this.onLoad();
			}
			@Override
			public View onCreateSuccessView() {
				return HomeDetails.this.onCreateSuccessView();
			}
		};
		setContentView(view);
		if(view!=null){
			//调用我们写的回调函数onload进行请求网络的操作
			view.show();
		}
		
		initActionBar();
	}
	
	protected  View onCreateSuccessView() {
		//根据布局去写view
		View view=UIUtils.inflate(R.layout.layout_detail);
		//顶部的app应用信息
		 fl_detail_appinfo=(FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
		 //一个模块对应一个holder
		 AppinfoHolder appinfoHolder = new AppinfoHolder();
		 appinfoHolder.setData(data);//把数据传递给holder对象
		 fl_detail_appinfo.addView(appinfoHolder.getRootView());
		 
		//app应用安全信息
		 fl_detail_safe=(FrameLayout) view.findViewById(R.id.fl_detail_safe);
		 //扩展的功能  TODO 添加ScrollView自动滑动到底端的效果
		 AppSafeHolder appSafeHolder = new AppSafeHolder();
		 appSafeHolder.setData(data);
		 fl_detail_safe.addView(appSafeHolder.getRootView());
		 
		//水平的图片
		hsv_detail_apppic = (HorizontalScrollView) view.findViewById(R.id.hsv_detail_apppic);
		AppPicHolder appPicHolder = new AppPicHolder();
		appPicHolder.setData(data);
		hsv_detail_apppic.addView(appPicHolder.getRootView());
		
		//底部描述的信息
		fl_deatil_desc = (FrameLayout) view.findViewById(R.id.fl_deatil_desc);
		AppdescHolder appdescHolder = new AppdescHolder();
		appdescHolder.setData(data);
		fl_deatil_desc.addView(appdescHolder.getRootView());
		
		//下载布局
		fl_download=(FrameLayout)view.findViewById(R.id.fl_download);
		downloadHolder = new DownloadHolder();
		downloadHolder.setData(data);
		downloadHolder.registerObserver();
		fl_download.addView(downloadHolder.getRootView());
		
		return view;
	}

	protected  ResultState onLoad() {
		String packageName = getIntent().getStringExtra("packageName");
		HomeDetailsProtocol homeDetailsProtocol = new HomeDetailsProtocol();
		homeDetailsProtocol.setPackageName(packageName);
		data = homeDetailsProtocol.getData(0);
		if(data!=null){
			return ResultState.REQUEST_SUCCESS;
		}else{
			return ResultState.REQUEST_ERROR;
		}
	}
	
	//--------ActionBar---------------------
	private void initActionBar() {
		actionBar=getSupportActionBar();
		
		actionBar.setTitle(UIUtils.getString(R.string.app_name));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		//
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//当菜单项按钮被选中时干么 如果我们选中的id==默认的android.R.id.home结束当前activity TODO如何配置
		if(item.getItemId()==android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onDestroy() {
		downloadHolder.unregisterObserver();
		super.onDestroy();
	}
	
}
