package com.google.paly.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.paly.bean.AppInfo;

public class GameProtocol extends BaseProtocol<List<AppInfo>> {
	private List<AppInfo> applist = new ArrayList<AppInfo>();
	@Override
	public String getKey() {
		return "game";
	}

	@Override
	public String getParams() {
		return null;
	}

	@Override
	public List<AppInfo> prcessData(String data) {
		if (data != null) {
			try {
				JSONArray jsonArray = new JSONArray(data);
				applist.clear();
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
						applist.add(appInfo);
					}
				return applist;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
