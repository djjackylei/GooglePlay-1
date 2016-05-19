package com.google.paly.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;


public class RecommendProtocol extends BaseProtocol<List<String>> {
	private List<String> list=new ArrayList<String>();
	@Override
	public String getKey() {
		return "recommend";
	}

	@Override
	public String getParams() {
		return null;
	}

	@Override
	public List<String> prcessData(String data) {
		try {
			list.clear();
			JSONArray jsonArray=new JSONArray(data);
			for(int i=0;i<jsonArray.length();i++){
				list.add(jsonArray.getString(i));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
