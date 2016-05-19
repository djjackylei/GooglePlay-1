package com.google.paly.bean;

import java.io.File;

import com.google.paly.manager.DownloadManager;

import android.os.Environment;

/**
 * appinfo转换成downloadinfo
 * @author yanbinadmin
 *
 */
public class DownloadInfo {
	//下载的javabean，填充那些字段
	
	//下载apk的唯一性id
	private String id;
	//当前下载到的位置 (断点续传)
	private long curretPosition;
	//下载到的位置，转换成float的百分比   下载到的百分比 当前位置/总的大小
	private float progress;
	//当前对象的下载状态  当前状态 
	private int currentState;
	//下载地址  
	private String downloadUrl;
	//下载apk的名称
	private String name;
	//下载apk的包名   用于当下载完成时用于打开安装界面传递的包名
	private String packageName;
	//下载apk的大小
	private long size;
	//下载apk的路径     
	private String path;
	
	private static String GOOGLEMARKET = "googleplay";
	private static String DOWNLOAD = "download";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCurretPosition() {
		return curretPosition;
	}

	public void setCurretPosition(long curretPosition) {
		this.curretPosition = curretPosition;
	}

	//进度条的百分比计算出来
	public float getProgress() {
		if(getSize()==0){
			return 0;
		}
		return (getCurretPosition()+0.0f)/getSize();
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	//TODO 将AppInfo转换成DownloadInfo 
	public static DownloadInfo copy(AppInfo appInfo){
		DownloadInfo downloadInfo = new DownloadInfo();
		//初始状态
		downloadInfo.setCurrentState(DownloadManager.STATE_NONE);
		
		downloadInfo.setCurretPosition(0);
		downloadInfo.setProgress(0);
		downloadInfo.setDownloadUrl(appInfo.getDownloadUrl());
		downloadInfo.setId(appInfo.getId());
		downloadInfo.setName(appInfo.getName());
		downloadInfo.setPackageName(appInfo.getPackageName());
		downloadInfo.setSize(appInfo.getSize());
		//sd卡上
		if(getFilePath()!=null){
			downloadInfo.setPath(getFilePath()+downloadInfo.getName()+".apk");
		}
		return downloadInfo;
	}
	//将下载路径定义出来
	public static String getFilePath(){
		StringBuffer sb = new StringBuffer();
		String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		//mnt/sdcard/googleplay/download/xx.apk
		sb.append(sdCardPath);
		sb.append(File.separator);
		sb.append(GOOGLEMARKET);
		sb.append(File.separator);
		sb.append(DOWNLOAD);
		sb.append(File.separator);
		if(createFile(sb.toString())){
			//文件创建成功
			return sb.toString();
		}else{
			return null;
		}
	}
	
	private static boolean createFile(String filePath) {
		if(filePath!=null){
			File file = new File(filePath);
			if(!file.exists() || !file.isDirectory()){
				return file.mkdirs();
			}
		}
		return true;
	}
}
