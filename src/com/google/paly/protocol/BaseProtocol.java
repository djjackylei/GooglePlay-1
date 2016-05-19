package com.google.paly.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.text.TextUtils;

import com.google.paly.http.HttpHelper;
import com.google.paly.http.HttpHelper.HttpResult;
import com.google.paly.utils.IOUtils;
import com.google.paly.utils.StringUtils;
import com.google.paly.utils.UIUtils;

public abstract class BaseProtocol<T>{
	//封装每页数据的网络请求
	/**
	 *获取当前指向模块部分链接地址的方法,例如"home"
	 * @return
	 */
	public abstract String getKey();
    /**
     * 返回拼接以后参数的方法 
     * @return
     */
	public abstract String getParams();
	/**
	 *  请求网络获取数据的方法
	 * 先从缓存文件中获取 如果没有网络 那么就显示缓存的信息
	 * 如果有网络在从网络中获取最新的数据 在缓存到网络 刷行数据
	 * 用得着每次都重修获取数据吗 ？设置一个缓存有效期 如果缓存在有效期内 那就直接使用缓存  
	 * 如果过期在从网络中获取
	 * 
	 * @param index
	 * @return
	 */
	public T getData(int index){
		//缓存文件中存储的从网络中获取的数据
		String data=getDataFromLocal(index);
		if(TextUtils.isEmpty(data)){
			//从网络获取
			data=getDataFromNet(index);
		}
		return prcessData(data);
	}
	/**
	 * 从网络中获取数据 成功就将数据缓存到本地
	 * @param index
	 * @return
	 */
	private String getDataFromNet(int index) {
		HttpHelper helper=new HttpHelper();
		//http://127.0.0.1:8090/home?index=0&key=value
		String getparams=getParams();
		if(getparams==null){//容错处理
			getparams="";
		}
										//HttpHelper.URL+getKey()+"?index="+index+getParams());
		HttpResult httpResult = helper.get(HttpHelper.URL+getKey()+"?index="+index+getparams);//getParams()
		String data = null;
		if(httpResult!=null)
			data=httpResult.getString();
		if(!TextUtils.isEmpty(data)){
			//缓存到本地
			write2Local(data,index);
		}
		return data;
	}
	/**
	 * 将网络中获取的最新数据缓存到本地
	 * @param data
	 * @param index
	 */
	private void write2Local(String data,int index) {
		File file=new File(UIUtils.getContext().getCacheDir(),getKey()+index+getParams());
		BufferedWriter writer=null;
		try{
			writer=new BufferedWriter(new FileWriter(file));
			//写入时间戳 有效时间60秒
			long available_time=System.currentTimeMillis()+30*60*1000;
			writer.write(available_time+"\r\n");
			writer.write(data);
			writer.flush();//将缓存中的数据写入文件
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			IOUtils.close(writer);
		}
		
	}
	/**
	 * 从文件获取数据
	 * @return
	 */
	private String getDataFromLocal(int index) {
	    //根据取到当前页面的索引去存储文件     模块名称+索引数+参数
		File file=new File(UIUtils.getContext().getCacheDir(),getKey()+index+getParams());
		//当前数据有效当前数据的第一行就作为时间戳 判断数据是否有效
		if(file.exists()){
			BufferedReader reader=null;
			try{
				reader=new BufferedReader(new FileReader(file));
				//TODO　做判断网络的操作　如果用户没有开启网络　那么不管数据有效不有效　都使用缓存　
				//有效时间
				long available_time=Long.valueOf(reader.readLine());
				//判断当前有效时间 
				if(available_time -System.currentTimeMillis()>0){
					//数据有效
					//获取缓存中的数据
					String temp;
					StringBuffer sb=new StringBuffer();
					while((temp=reader.readLine())!=null){
						sb.append(temp);
					}
					return sb.toString();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				IOUtils.close(reader);
			}
		}
		return null;
	}
	/**
	 * 解析数据的方法
	 * @param data 要进行解析的数据
	 * @return
	 */
	public abstract T prcessData(String data);
}
