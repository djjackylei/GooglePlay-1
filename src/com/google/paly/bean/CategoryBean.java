package com.google.paly.bean;

import java.util.List;

public class CategoryBean {
	public String title;
	public boolean isTitle;
	public boolean isName1;
	public boolean isName2;
	public boolean isName3;
	//public List<Infos> infos;
	//public class Infos{
	public String name1;
	public String name2;
	public String name3;
	public String url1;
	public String url2;
	public String url3;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isTitle() {
		return isTitle;
	}
	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	public String getUrl3() {
		return url3;
	}
	public void setUrl3(String url3) {
		this.url3 = url3;
	}
 
	// "infos":
	// "name1": "休闲",
	// "name2": "棋牌",
	// "name3": "益智",
	// "url1": "image/category_game_0.jpg",
	// "url2": "image/category_game_1.jpg",
	// "url3": "image/category_game_2.jpg"
}
