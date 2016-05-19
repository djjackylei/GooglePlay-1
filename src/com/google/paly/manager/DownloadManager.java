package com.google.paly.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.paly.bean.AppInfo;
import com.google.paly.bean.DownloadInfo;
import com.google.paly.http.HttpHelper;
import com.google.paly.http.HttpHelper.HttpResult;
import com.google.paly.utils.IOUtils;
import com.google.paly.utils.UIUtils;

import android.content.Intent;
import android.net.Uri;

public class DownloadManager {
	//1,默认状态
	public static final int STATE_NONE = 1;
	//2,等待执行执行状态 线程开启的比较多时  肯定不能同时取下载 线程池中只有5个线程 最多只能有5个任务同时下载  
	public static final int STATE_WAITTING = 2;
	//3，下载状态
	public static final int STATE_DOWNLOADING = 3;
	//4,暂停
	public static final int STATE_PAUSE = 4;
	//5，失败
	public static final int STATE_ERROR = 5;
	//6,成功状态
	public static final int STATE_DOWNLOADED = 6;
	
	//单例模式 
	private DownloadManager(){};
	
	//创建唯一的一个对象
	private static DownloadManager downloadManager = new DownloadManager();
	//提供一个方法返回对应对象
	public static DownloadManager getInstance(){
		return downloadManager;
	}
	
	//封装下载过的对象的集合,如果是真实开发需要去 TODO 创建数据库存储对应的DownloadInfo信息
	private Map<String,DownloadInfo> downloadInfoMap = new HashMap<String, DownloadInfo>();
	//
	private List<DownloadObserver> observerList = new ArrayList<DownloadObserver>();
	
	//下载任务所在的集合
	private Map<String,DownloadTask> downloadTaskMap = new HashMap<String,DownloadTask>();
	
	//下载过程中伴随着状态的改变，进度条的改变，观察者模式(如果有跟之前操作有差异的话，就需要告知UI)
	
	public DownloadInfo getDownloadInfo(String id){
		return downloadInfoMap.get(id);
	}
	
	//注册观察者的方法 根据状态或者进度条的改变 去通知ui更新 回调 ()就是观察者模式
	public void registerObserver(DownloadObserver downloadObserver){
		if(downloadObserver!=null){
			//1.1，添加对应观察者           
			if(!observerList.contains(downloadObserver)){
				observerList.add(downloadObserver);
			}
		}
	}
	//反注册观察者
	public void unRegisterObserver(DownloadObserver downloadObserver){
		if(downloadObserver!=null){
			//将当前条目注册的观察者对象从集合中移除
			if(observerList.contains(downloadObserver)){
				observerList.remove(downloadObserver);
			}
		}
	}
	
	//通知进度条或者状态发生改变的方法  状态发生改变的时候调用的方法
	public void notifyDownloadStateChange(DownloadInfo downloadInfo){
		//通知哪个条目的状态发生改变 通过id来知道是哪个条目发生改变
		//2，等到状态切换的时候，调用了当前方法
		if(downloadInfo!=null){
			//2.1 循环遍历
			for(DownloadObserver downloadObserver:observerList){
				//告知哪个javabean做对应状态转变操作 
				//一个downloadobserver对应一个downloadInfo
				//1downloadInfo->有缘网  
				//2downloadInfo->酷狗 
				//2.2，观察者中的具体实现 
				downloadObserver.onDownloadStateChange(downloadInfo);
			}
		}
	}
	
	//通知下载状态发生改变的方法     进度条发生改变的时候调用的方法
	public void notifyDownloadProgressChange(DownloadInfo downloadInfo){
		if(downloadInfo!=null){
			for(DownloadObserver downloadObserver:observerList){
				//告知哪个javabean做对应状态转变操作
				downloadObserver.onDownloadProgressChange(downloadInfo);
			}
		}
	}
	

	//创建一个观察者对象(状态的改变，监听进度条的改变)
	public interface DownloadObserver{
		//因为不同界面处理进度条和文字切换的UI处理方式不同,所以没有具体实现 
		//下载状态发生改变的方法 
		public void onDownloadStateChange(DownloadInfo downloadInfo);
		//下载过程中进度条发生改变的方法
		public void onDownloadProgressChange(DownloadInfo downloadInfo);
	}
	/**
	 * 下载的是哪一个apk  传递appinfo是为了确认下载的是哪一个apk
	 * @param appInfo
	 */
	public synchronized void download(AppInfo appInfo){
		if(appInfo!=null){
			DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
			if(downloadInfo==null){
				//之前没有下载过当前的应用，重新构建downloadInfo对象，用作下载，并且将其维护在map集合中
				downloadInfo = DownloadInfo.copy(appInfo);
				downloadInfoMap.put(appInfo.getId(), downloadInfo);
				
				//下载逻辑,初始将状态变为等待，并且通知UI改变
				downloadInfo.setCurrentState(STATE_WAITTING);
				notifyDownloadStateChange(downloadInfo);
			}
			//下载任务的维护   任务中进行了状态的改变 -
			DownloadTask downloadTask = new DownloadTask(downloadInfo);
			ThreadManager.getThreadPoolProxy().execute(downloadTask);
			downloadTaskMap.put(downloadInfo.getId(), downloadTask);
		}
	}
	
	
	public synchronized void pause(AppInfo appInfo){
		if(appInfo!=null){
			DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
			stopDownload(appInfo);
			//由下载状态，等待状态-->变成暂停，
			int currentState = downloadInfo.getCurrentState();
			if(currentState == STATE_WAITTING || 
					currentState == STATE_DOWNLOADING){
				//改变状态  通知UI做改变
				downloadInfo.setCurrentState(STATE_PAUSE);
				//
				notifyDownloadStateChange(downloadInfo);
			}
		}
	}
	
	//安装方法，安装一个新的apk
	public synchronized void install(AppInfo appInfo){
		//转换成AppInfo--->downloadInfo,下载apk的存储路径，开启activity做安装操作
		stopDownload(appInfo);
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()),"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(intent);
		}
	}

	private void stopDownload(AppInfo appInfo) {
		//从存储任务的线程池中取出任务
		DownloadTask downloadTask = downloadTaskMap.get(appInfo.getId());
		//从线程池中移除任务的对象 
		if(downloadTask!=null){
			ThreadManager.getThreadPoolProxy().cancel(downloadTask);
		}
	}
	
	class DownloadTask implements Runnable{
		private DownloadInfo downloadInfo;
		//构造方法中需要传递DownloadInfo对象，去确定下载的是那个一个apk
		public DownloadTask(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}
		public void run() {
			//正在下载操作,线程等待状态---->下载状态
			HttpResult httpResult;
			//改变当前apk的下载状态  通知观察者状态改变
			downloadInfo.setCurrentState(STATE_DOWNLOADING);
			notifyDownloadStateChange(downloadInfo);
			
			//1,重头下载
			String filePath = downloadInfo.getPath();
			File file = new File(filePath);
			//区分是断点续传还是重新下载  
			if(!file.exists() || file.length()!=downloadInfo.getCurretPosition() 
					|| downloadInfo.getCurretPosition() == 0){
				//第一次下载 ：文件不存在 或者文件长度！=和下载保存的进度 或者下载进度=0
				httpResult =  HttpHelper.get(HttpHelper.URL+"download?name="+downloadInfo.getDownloadUrl());
				//原有文件删除   原来就有但是不符合要求
				file.delete();
				downloadInfo.setCurretPosition(0);
			}else{
				//2,断点续传 两者方式 第一种在本地记录你对应拆分文件已经下载的位置   2让服务器去记录我现在读取到的位置
				httpResult = HttpHelper.get(HttpHelper.URL+"download?name="+downloadInfo.getDownloadUrl()
						+"&range="+downloadInfo.getCurretPosition());
			}
			if(httpResult!=null && httpResult.getInputStream()!=null){
				//读取流数据操作
				FileOutputStream fileOutputStream = null;
				InputStream inputStream = null;
				try {
					//true 续上原有的文件数据，接着向后写入   可追加的
					fileOutputStream = new FileOutputStream(file,true);
					inputStream = httpResult.getInputStream();
					
					byte[] buffer = new byte[1024];
					int temp = -1;
					//写入文件的时候是可追加的   用于停止线程通过逻辑控制是否下载 downloadInfo.getCurrentState() == STATE_DOWNLOADING
					while((temp = inputStream.read(buffer))!=-1 && downloadInfo.getCurrentState() == STATE_DOWNLOADING){
						fileOutputStream.write(buffer, 0, temp);
						fileOutputStream.flush();
						
						//更新当前apk的下载进度  
						downloadInfo.setCurretPosition(downloadInfo.getCurretPosition()+temp);
						//通知下载进度变化了
						notifyDownloadProgressChange(downloadInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					//下载出错
					//删除已经下载的文件
					file.delete();
					downloadInfo.setCurretPosition(0);
					//修改下载状态
					downloadInfo.setCurrentState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);
				}finally{
					//关闭流，关闭连接操作
					IOUtils.close(fileOutputStream);
					IOUtils.close(inputStream);
					
					if(httpResult!=null){
						httpResult.close();
					}
				}
				//下载完毕，准备安装 只有下载的进度和apk大小一致 
				if(downloadInfo.getCurretPosition() == downloadInfo.getSize()){
					downloadInfo.setCurrentState(STATE_DOWNLOADED);
					notifyDownloadStateChange(downloadInfo);
				}else if(downloadInfo.getCurrentState() == STATE_PAUSE){
					//下载暂停
					notifyDownloadStateChange(downloadInfo);
				}else{
					//删除已经下载的文件
					file.delete();
					downloadInfo.setCurretPosition(0);
					//修改下载状态
					downloadInfo.setCurrentState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);
				}
				//下载出错
			}else{
				//流都没有    流没有可能是没网络 干嘛要删？！
				//删除已经下载的文件
				file.delete();
				downloadInfo.setCurretPosition(0);
				//修改下载状态
				downloadInfo.setCurrentState(STATE_ERROR);
				notifyDownloadStateChange(downloadInfo);
			}
			
			//移除现有执行完操作的任务   下载结束 从下载集合中移除任务 
			downloadTaskMap.remove(downloadInfo.getId());
		};
	}
}
