package com.google.paly.bean;

import java.util.ArrayList;
import java.util.List;

public class AppInfo {
//	   "picture": [
	//轮播图相关代码 
	//这么设置很麻烦!! TODO数据一多就存入集合中 
//	private String picurl1;
//	private String picurl2;
//	private String picurl3;
//	private String picurl4;
//	private String picurl5;
//	private String picurl6;
//	private String picurl7;
	
//	               "image/home01.jpg",
//	               "image/home02.jpg",
//	               "image/home03.jpg",
//	               "image/home04.jpg",
//	               "image/home05.jpg",
//	               "image/home06.jpg",
//	               "image/home07.jpg",
//	               "image/home08.jpg"
	//           ],

	private String id;
	private String name;
	private String packageName;
	private String iconUrl;
	private float stars;
	private long size;
	private String downloadUrl;
	private String des;
	
	
	private String downloadNum;
	private String version;
	private String date;
	private String author;
	
	//详细页的图片的集合 
	private List<String> screenList = new ArrayList<String>();
	//创建safeurl的集合 文字
	private List<String> safeUrlList= new ArrayList<String>();
	//创建sagedesurl集合 图片
	private List<String> safeDesUrlList= new ArrayList<String>();
	//创建sagedes的集合
	private List<String> safeDesList= new ArrayList<String>();
	
	
	
	// "downloadUrl": "app/com.youyuan.yyhl/com.youyuan.yyhl.apk",
	//"des": "产品介绍：有缘是时下最受大众单身男女亲睐的婚恋交
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<String> getScreenList() {
		return screenList;
	}
	public void setScreenList(List<String> screenList) {
		this.screenList = screenList;
	}
	public List<String> getSafeUrlList() {
		return safeUrlList;
	}
	public void setSafeUrlList(List<String> safeUrlList) {
		this.safeUrlList = safeUrlList;
	}
	public List<String> getSafeDesUrlList() {
		return safeDesUrlList;
	}
	public void setSafeDesUrlList(List<String> safeDesUrlList) {
		this.safeDesUrlList = safeDesUrlList;
	}
	public List<String> getSafeDesList() {
		return safeDesList;
	}
	public void setSafeDesList(List<String> safeDesList) {
		this.safeDesList = safeDesList;
	}
	
	
}
