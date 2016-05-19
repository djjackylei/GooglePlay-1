package com.google.paly.base;

import java.util.List;

import com.google.paly.holder.MoreHolder;
import com.google.paly.manager.ThreadManager;
import com.google.paly.utils.UIUtils;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	private List<T> list;
	//普通条目
	private static final int LIST_ITEM=0;
	//加载更多条目
	private static final int LOAD_MODR=1;
	private MoreHolder moreholder;
	public MyBaseAdapter(List<T> list) {
		this.list = list;
	}
	public List<T> getList(){
		return list;
	}
	@Override
	public int getCount() {
		return list.size()+1;
	}
	public int getListSize(){
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getViewTypeCount() {
		//TODO 条目类型个数不要忘了
		return super.getViewTypeCount()+1;
	}
	@Override
	public int getItemViewType(int position) {
		if(position==getCount()-1){
			//返回加载更多的条目 将getCount的返回值加1因为多了最后一个条目
			return LOAD_MODR;
		}else {
			//为了扩展性 子类adapter有多个条目 定义一个公有的可覆盖的方法
			return getInnerItemType(position);
		}
	}
	/**
	 * 子类可覆盖此方法用于listitem有多个 条目的情况
	 */
	public int getInnerItemType(int position) {
		//返回普通条目
		return LIST_ITEM;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 将下面的代码进行抽取 分析哪些是可以却定的 哪些是不确定的 不确定的就抽象 交给子类去实现
		BaseHolder holder = null;// 不确定 hodler里有什么控件 抽象
		if (convertView == null) {// 固定
		//--------------这几步全部放在BaseHolder中的构造方法中去做-----------
			// convertView=UIUtils.inflate(android.R.layout.simple_list_item_1);//布局不确定抽象 xml-->view 放到Baseholder中去做
			// holder=new ViewHolder();//固定的 创建ViewHolder BaseHolder是抽象类所以要获得他的子类 此方法必须是抽象方法
			if(getItemViewType(position)==LOAD_MODR){
				holder=getMoreHolder();//注意加载更多条目每个界面的条目是固定的
			}else{
			//if(getItemViewType(position)==LIST_ITEM){//这边注释掉 这样一般条目 和 其他条目都会创建各自的条目
				holder=getHolder();//在这里面调用
			}
		//	convertView=holder.getRootView();//TODO 不能放在这儿
			// holder.tv=(TextView)
			// convertView.setTag(holder);//固定
			// convertView.findViewById(android.R.id.text1);//抽象不确定view中有什么控件
		//-------------------------------------------------
		 }else{
			 holder=(BaseHolder)convertView.getTag();//固定的
		 }
		//TODO 因为有两种甚至更多种条目 所以设置数据 要进行判断
		if(getItemViewType(position)==LIST_ITEM){
			holder.setData(list.get(position));////获取数据 数据是固定的list.get(position)
		// holder.tv.setText(list.get(position));
		// 设置到控件上 不确定 抽象 将数据绑定到控件上() 
		//setData2View(); 此方法放在哪啊 谁拥有数据谁就拥有对外操作数据的方法  控件事件BaseHolder的子类中的定义的 所以将数据设置到空间的方法也应该在baseHolder中
		}
		return holder.getRootView();
	}

	/**
	 * 返回加载更多的holder
	 * @return
	 */
	private MoreHolder getMoreHolder() {
		if(moreholder==null) //避免重复创建对象
			moreholder = new MoreHolder(isHasMore(),this);
		return moreholder;
	}
	/**
	 * 设置是否还有数据的状态 默认有数据  可以给子类覆盖 如果不需要从服务器获取数据的话
	 * @return
	 */
	public Boolean isHasMore() {
		return true;
	}

	public abstract BaseHolder<T> getHolder();
	/**
	 * 根据访问网络获取的结果改变MoreHolder的状态  此处处理业务逻辑 具体的访问网络的请求地址..不确定 所以使用抽象 让子类去实现
	 */
	public void loadMore() {
//		//访问网络所以开启子线程
//		new Thread(){ //所有开启子线程的都使用线程管理类
//			public void run() {
//				//访问网络返回的数据还是一般条目
//				final List<T> more_list=onLoad();//加载网络获取的路径未知所有抽象
//				// 如果假定服务器一次返回的数据是二十条
//				//那么就有三种状态 1服务器返回少于20条 那么代表服务器没有更多数据了 将状态置为NO_MORE
//				//2服务器返回20 条 那么还有更多数据 HAS_MORE
//				//3服务器请求失败 LOAD_ERROR
//				//更新ui在使用UIUTils
//				UIUtils.runInMainThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						if(more_list!=null){
//							if(more_list.size()==20){
//								moreholder.setData(MoreHolder.LOAD_MORE);
//							}
//							if(more_list.size()<20){
//								moreholder.setData(MoreHolder.NO_MORE);
//							}
//						}else{
//							moreholder.setData(MoreHolder.LOAD_ERROR);
//						}
//						if(more_list!=null){
//							//将数据加入到当前listview的集合中 
//							list.addAll(more_list);
//							notifyDataSetChanged();//更新ListView
//						}
//						
//					}
//				});
//				
//			}
//		}.start();
		ThreadManager.getThreadPoolProxy().execute(new RunnableTask());
	}
	class RunnableTask implements Runnable{

		@Override
		public void run() {
			//访问网络返回的数据  是一般条目
			final List<T> more_list=onLoad();//加载网络获取的路径未知所有抽象
			// 如果假定服务器一次返回的数据是二十条
			//那么就有三种状态 1服务器返回少于20条 那么代表服务器没有更多数据了 将状态置为NO_MORE
			//2服务器返回20 条 那么还有更多数据 HAS_MORE
			//3服务器请求失败 LOAD_ERROR
			//更新ui在使用UIUTils
			UIUtils.runInMainThread(new Runnable() {
				
				@Override
				public void run() {
					if(more_list!=null){
						if(more_list.size()==20){
							moreholder.setData(MoreHolder.LOAD_MORE);
						}
						if(more_list.size()<20){
							moreholder.setData(MoreHolder.NO_MORE);
						}
					}else{
						moreholder.setData(MoreHolder.LOAD_ERROR);
					}
					if(more_list!=null){
						//将数据加入到当前listview的集合中 
						list.addAll(more_list);
						notifyDataSetChanged();//更新ListView
					}
					
				}
			});
			
		}
		
	}
	
	
	/**
	 * 访问网络获取更多的数据
	 * @return
	 */
	public abstract List<T> onLoad();
}
