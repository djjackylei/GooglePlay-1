package com.google.paly.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.paly.bean.CategoryBean;
import com.google.paly.utils.LogUtils;

public class CategoryProtocol extends BaseProtocol<List<CategoryBean>> {
	private List<CategoryBean> list=new ArrayList<CategoryBean>();

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public String getParams() {
		return null;
	}

	@Override
	public List<CategoryBean> prcessData(String data) {
		try {
		if(data!=null){
			list.clear();
			JSONArray array = new JSONArray(data);
			for(int i=0;i<array.length();i++){
				JSONObject jsonObject=array.getJSONObject(i);
				if(jsonObject.has("title")){
					CategoryBean bean=new CategoryBean();
					bean.setTitle(jsonObject.getString("title"));
					bean.setTitle(true);
					//添加标题
					list.add(bean);
				}
				
				if(jsonObject.has("infos")){
					//infos 又是数组
					JSONArray jsonArray=jsonObject.getJSONArray("infos");
					for(int j=0;j<jsonArray.length();j++){
						CategoryBean bean=new CategoryBean();
						JSONObject jsonObject2=jsonArray.getJSONObject(j);
						bean.setName1(jsonObject2.getString("name1"));
						bean.setName2(jsonObject2.getString("name2"));
						bean.setName3(jsonObject2.getString("name3"));
						
						bean.setUrl1(jsonObject2.getString("url1"));
						bean.setUrl2(jsonObject2.getString("url2"));
						bean.setUrl3(jsonObject2.getString("url3"));
						//添加条目
						list.add(bean);
					}
				}
			}
			return list;
			//使用Gson只要两行代码
//			Gson gson=new Gson();
//			List<CategoryBean> list = gson.fromJson(data, new TypeToken<List<CategoryBean>>(){}.getType());
		//	return list;
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
