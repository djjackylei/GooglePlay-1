package com.google.paly.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.test.UiThreadTest;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.paly.HomeDetails;
import com.google.paly.R;
import com.google.paly.base.BaseFragment;
import com.google.paly.base.BaseHolder;
import com.google.paly.base.MyBaseAdapter;
import com.google.paly.bean.AppInfo;
import com.google.paly.holder.HomeHeaderHolder;
import com.google.paly.holder.HomeHolder;
import com.google.paly.protocol.BaseProtocol;
import com.google.paly.protocol.HomeProtocol;
import com.google.paly.ui.widgest.MyListView;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.utils.UIUtils;

public class HomeFragment extends BaseFragment {
	private List<AppInfo> list=new ArrayList<AppInfo>();
	private List<String> picList;
	@Override
	public View onCreateSuccessView() {
		
		//TODO  每个界面都要使用到ListView 使用代码去除ListView的黑色背景 去掉选中背景颜色 去掉分割线
		ListView lv=new MyListView(UIUtils.getContext());
		if(lv.getHeaderViewsCount()<1){
			HomeHeaderHolder homeHeaderHolder = new HomeHeaderHolder(picList);
			lv.addHeaderView(homeHeaderHolder.getRootView());
		}
		//TODO 先添加头再去设置数据设配器
//		list = homeProtocol.getData(0);
		lv.setAdapter(new HomeAdapter(list,lv));
		return lv;
	}


	@Override
	public ResultState onLoad() {
		HomeProtocol homeProtocol=new HomeProtocol();
		//根据已经有的条目个数告诉服务器下次从哪取数据
		list = homeProtocol.getData(0);
		picList = homeProtocol.getPicList();
		//每个view都需要检测从服务器获取的数据是否是空 还是错误 或success 所以放到父类去做
		return check(list);
	}

	//TODO  抽取BaseAdapter 模板设计模式
	class HomeAdapter extends MyBaseAdapter<AppInfo> implements OnItemClickListener{

		private ListView listView;
		public HomeAdapter(List<AppInfo> list,ListView listView) {
			super(list);
			this.listView=listView;
			listView.setOnItemClickListener(this);
		}
		@Override
		public BaseHolder<AppInfo> getHolder() {
			return new HomeHolder();
		}
		@Override
		public List<AppInfo> onLoad() {
			//TODO  抽取网络协议 BaseProtocol 已完成
			BaseProtocol<List<AppInfo>> homeProtocol=new HomeProtocol();
			//根据已经有的条目个数告诉服务器下次从哪取数据
			List<AppInfo> data = homeProtocol.getData(getListSize());
			return data;
		}
		//给listitem设置了点击事件 但是却不响应  因为ratingbar抢夺焦点了将其抢夺焦点的操作屏蔽掉
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//因为还有个头 所以position-1 将当前应用程序包名传过去
			String packageName = list.get(position-1).getPackageName();
			//
			Intent intent=new Intent(UIUtils.getContext(),HomeDetails.class);
			intent.putExtra("packageName", packageName);
			startActivity(intent);
		}

	}
}
