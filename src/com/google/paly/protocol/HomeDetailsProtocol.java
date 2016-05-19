package com.google.paly.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.LinearLayout;

import com.google.paly.bean.AppInfo;
import com.google.paly.utils.UIUtils;

public class HomeDetailsProtocol extends BaseProtocol<AppInfo> {

	private String packageName;
	private List<String> screenList=new ArrayList<String>();
	private List<String> safeUrlList=new ArrayList<String>();
	private List<String> safeDesList=new ArrayList<String>();
	private List<String> safeDesUrlList=new ArrayList<String>();
	@Override
	public String getKey() {
		
		return "detail";
	}

	@Override
	public String getParams() {
		return "&packageName="+packageName;
	}
	//解析json的数据
	@Override
	public AppInfo prcessData(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			
			AppInfo appInfo = new AppInfo();
			appInfo.setDes(jsonObject.getString("des"));
			appInfo.setDownloadUrl(jsonObject.getString("downloadUrl"));
			appInfo.setIconUrl(jsonObject.getString("iconUrl"));
			appInfo.setId(jsonObject.getString("id"));
			appInfo.setName(jsonObject.getString("name"));
			appInfo.setPackageName(jsonObject.getString("packageName"));
			appInfo.setSize(jsonObject.getLong("size"));
			appInfo.setStars((float)jsonObject.getDouble("stars"));
			
			appInfo.setAuthor(jsonObject.getString("author"));
			appInfo.setDownloadNum(jsonObject.getString("downloadNum"));
			appInfo.setVersion(jsonObject.getString("version"));
			appInfo.setDate(jsonObject.getString("date"));
			
			if(jsonObject.has("screen")){
				JSONArray jsonArray = jsonObject.getJSONArray("screen");
				screenList.clear();
				for(int i=0;i<jsonArray.length();i++){
					screenList.add(jsonArray.getString(i));
				}
				appInfo.setScreenList(screenList);
			}
			
			if(jsonObject.has("safe")){
				JSONArray jsonArray = jsonObject.getJSONArray("safe");
				safeUrlList.clear();
				safeDesList.clear();
				safeDesUrlList.clear();
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					
					safeUrlList.add(jsonObject2.getString("safeUrl"));
					safeDesList.add(jsonObject2.getString("safeDes"));
					safeDesUrlList.add(jsonObject2.getString("safeDesUrl"));
				}
				
				appInfo.setSafeUrlList(safeUrlList);
				appInfo.setSafeDesList(safeDesList);
				appInfo.setSafeDesUrlList(safeDesUrlList);
			}
			
			return appInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setPackageName(String packageName){
		this.packageName=packageName;
	}

}
