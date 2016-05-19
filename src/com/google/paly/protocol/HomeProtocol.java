package com.google.paly.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.paly.bean.AppInfo;

public class HomeProtocol extends BaseProtocol<List<AppInfo>> {
	private List<AppInfo> homelist = new ArrayList<AppInfo>();
	private List<String> piclist = new ArrayList<String>();

	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParams() {
		return null;
	}
	/**
	 * 获取所有轮播图的地址
	 * @return
	 */
	public List<String> getPicList(){
		//getData(0);
		if(piclist!=null){
			return piclist;
		}
		return null;
	}
	@Override
	public List<AppInfo> prcessData(String data) {
		if (data != null) {
			try {
				JSONObject jsonObject = new JSONObject(data);
				homelist.clear();
				if (jsonObject.has("list")) {
					JSONArray jsonArray = jsonObject.getJSONArray("list");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						AppInfo appInfo = new AppInfo();
						appInfo.setDes(jsonObject2.getString("des"));
						appInfo.setDownloadUrl(jsonObject2
								.getString("downloadUrl"));
						appInfo.setIconUrl(jsonObject2.getString("iconUrl"));
						appInfo.setName(jsonObject2.getString("name"));
						appInfo.setPackageName(jsonObject2
								.getString("packageName"));
						appInfo.setId(jsonObject2.getString("id"));
						appInfo.setSize(jsonObject2.getLong("size"));
						appInfo.setStars((float) jsonObject2.getDouble("stars"));
						homelist.add(appInfo);
					}
				}
				piclist.clear();
				if(jsonObject.has("picture")){
					JSONArray jsonArray = jsonObject.getJSONArray("picture");
					for (int i = 0; i < jsonArray.length(); i++) {
						piclist.add(jsonArray.getString(i));
					}
				}
				return homelist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
