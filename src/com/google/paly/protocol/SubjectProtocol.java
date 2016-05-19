package com.google.paly.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.paly.bean.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfo>> {
	private List<SubjectInfo> sub_data=new ArrayList<SubjectInfo>();
	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParams() {
		return null;
	}

	@Override
	public List<SubjectInfo> prcessData(String data) {
		try{
			if(data!=null){
				sub_data.clear();
				JSONArray jsonArray=new JSONArray(data);
				for(int i=0;i<jsonArray.length();i++){
					SubjectInfo info=new SubjectInfo();
					JSONObject object = jsonArray.getJSONObject(i);
					info.setUrl(object.getString("url"));
					info.setDes(object.getString("des"));
					sub_data.add(info);
				}
				return sub_data;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
