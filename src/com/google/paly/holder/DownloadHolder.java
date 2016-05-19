package com.google.paly.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.google.paly.R;
import com.google.paly.base.BaseHolder;
import com.google.paly.bean.AppInfo;
import com.google.paly.bean.DownloadInfo;
import com.google.paly.manager.DownloadManager;
import com.google.paly.manager.DownloadManager.DownloadObserver;
import com.google.paly.ui.widgest.ProgressHorizontal;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
/**
 * 使用到下载地址 包名。。
 * @author yanbinadmin
 */
public class DownloadHolder extends BaseHolder<AppInfo> implements OnClickListener, DownloadObserver{

	private FrameLayout fl_download;
	private Button bt_download;
	private AppInfo appInfo;
	private int state;
	private float progress;
	private ProgressHorizontal progressHorizontal;
	private DownloadManager downloadManager;

	@Override
	public void setData2View() {
		appInfo = getData();
		
		//根据setdata里面的初始状态去处理UI
		refreshMainThreadUI(state,progress);
	}
	/**
	 * 根据状态显示不同的UI
	 * @param state
	 * @param progress
	 */
	private void refreshMainThreadUI(int state,float progress) {
		this.state=state;
		this.progress=progress;
		
		switch (state) {
		//默认状态
		case DownloadManager.STATE_NONE:
			progressHorizontal.setVisibility(View.GONE);
			bt_download.setVisibility(View.VISIBLE);
			bt_download.setText(UIUtils.getString(R.string.app_state_download));
			break;
		case DownloadManager.STATE_DOWNLOADING:
			progressHorizontal.setVisibility(View.VISIBLE);
			bt_download.setVisibility(View.GONE);
			progressHorizontal.setCenterText("");
			//和上面一样
			progressHorizontal.setProgress(progress);
//			bt_download.setText(UIUtils.getString(R.string.app_state_download));
			break;
		case DownloadManager.STATE_ERROR:
			progressHorizontal.setVisibility(View.GONE);
			bt_download.setVisibility(View.VISIBLE);
			bt_download.setText(UIUtils.getString(R.string.app_state_error));
			break;
		case DownloadManager.STATE_PAUSE:
			progressHorizontal.setVisibility(View.VISIBLE);
			progressHorizontal.setCenterText(UIUtils.getString(R.string.app_state_paused));
			progressHorizontal.setProgress(progress);
			bt_download.setVisibility(View.GONE);
			break;
		case DownloadManager.STATE_WAITTING:
			progressHorizontal.setVisibility(View.GONE);
		//	progressHorizontal.setCenterText(UIUtils.getString(R.string.app_state_waiting));
			progressHorizontal.setProgress(progress);
			bt_download.setVisibility(View.VISIBLE);
			bt_download.setText(UIUtils.getString(R.string.app_state_waiting));
			break;
		case DownloadManager.STATE_DOWNLOADED:
			progressHorizontal.setVisibility(View.GONE);
		//	progressHorizontal.setCenterText(UIUtils.getString(R.string.app_state_downloaded));
			progressHorizontal.setProgress(progress);
			bt_download.setVisibility(View.VISIBLE);
			bt_download.setText(UIUtils.getString(R.string.app_state_downloaded));
			break;
		}
	}

	@Override
	public View initView() {
		View view=UIUtils.inflate(R.layout.layout_download);
		bt_download = (Button) view.findViewById(R.id.bt_download);
		//放置进度条的帧布局对象 
		fl_download = (FrameLayout) view.findViewById((R.id.fl_download));
		bt_download.setOnClickListener(this);
		fl_download.setOnClickListener(this);
		
		//使用递增的自定义进度条
		
		progressHorizontal = new ProgressHorizontal(UIUtils.getContext());
		//设置进度条内部文字是否显示  
		progressHorizontal.setProgressTextVisible(true);
		//设置进度条内部文字的大小 ，颜色
		progressHorizontal.setProgressTextColor(Color.WHITE);
		progressHorizontal.setProgressTextSize(UIUtils.dip2px(18));
		//设置进度条递增的前景图片  蓝色
		progressHorizontal.setProgressResource(R.drawable.progress_normal);
		//设置进度条递增的背景图片  黑色
		progressHorizontal.setBackgroundResource(R.drawable.progress_bg);
	
		//先设置规则
		LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//加入到帧布局
		fl_download.addView(progressHorizontal, layoutParams);
		
		return view;
	}
	
	//----------在setData时加一段逻辑在里面-----------------
	@Override
	public void setData(AppInfo data) {
		//在此处获取下载appinfo对应的downloadinfo对象
		String id=data.getId();
		//appinfo中id和downloadinfo中的id是相同的 所有可以通过id在downloadmanager中 
		//downloadInfoMap中找到downloadinfo对象
		//真实开发中应该使用数据库存储downloadinfo对象 而不是存储在内存中
		//一开始不是不存在吗？？！！
		
		downloadManager = DownloadManager.getInstance();
		DownloadInfo downloadInfo = downloadManager.getDownloadInfo(id);
		if(downloadInfo==null) {
			//没下过
			state=DownloadManager.STATE_NONE;
			progress=0;
		}else{
			state=downloadInfo.getCurrentState();
			progress=downloadInfo.getProgress();
		}
		super.setData(data);
	}
	//每次设置数据时读取之前是否下载过 如果没有 下载状态就是默认 下载进度为0
	//如果之前下载过从数据库中取出downloadInfo(此处以内存模拟数据库)
	// 获取下载状态(暂停？)和下载进度 
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_download:
		case R.id.fl_download:
			//如果现在对应的appinfo/downloadinfo的状态是（等待或下载 变成暂停）
			//居然在这座状态的切换 ！@！！
			if(state==DownloadManager.STATE_WAITTING||state==DownloadManager.STATE_DOWNLOADING){
				downloadManager.pause(appInfo);
			//downloadinfo对象状态是 暂停 出错或默认   -》下载
			}else if(state==DownloadManager.STATE_PAUSE||state==DownloadManager.STATE_ERROR
					||state==DownloadManager.STATE_NONE){
				downloadManager.download(appInfo);
			}else if(state==DownloadManager.STATE_DOWNLOADED){
				//如果是完成  -》安装
				downloadManager.install(appInfo);
			}
			break;
		}
	}
	//暴露注册内容观察者的方法
	public void registerObserver(){
		downloadManager.registerObserver(this);
	}
	
	public void unregisterObserver(){
		downloadManager.unRegisterObserver(this);
	}
	
	
	
	
	@Override
	public void onDownloadStateChange(DownloadInfo downloadInfo) {
		refreshUI(downloadInfo);//回调带参数     
	}
	
	private void refreshUI(final DownloadInfo downloadInfo) {
		if(downloadInfo.getId()==appInfo.getId()){//容错处理
			//把处理UI的操作放在主线程中去做
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					
					refreshMainThreadUI(downloadInfo.getCurrentState(), downloadInfo.getProgress());
				}
			});
		}
	}
	@Override
	public void onDownloadProgressChange(DownloadInfo downloadInfo) {
		refreshUI(downloadInfo);
	}
}
