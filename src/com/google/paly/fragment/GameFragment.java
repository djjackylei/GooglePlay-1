package com.google.paly.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.google.paly.base.BaseFragment;
import com.google.paly.base.BaseHolder;
import com.google.paly.base.MyBaseAdapter;
import com.google.paly.bean.AppInfo;
import com.google.paly.holder.AppHolder;
import com.google.paly.protocol.AppProtocol;
import com.google.paly.protocol.BaseProtocol;
import com.google.paly.protocol.GameProtocol;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.ui.widgest.MyListView;
import com.google.paly.utils.UIUtils;

public class GameFragment extends BaseFragment {
	private List<AppInfo> game_list=new ArrayList<AppInfo>();
	@Override
	public View onCreateSuccessView() {
		ListView list=new MyListView(UIUtils.getContext());
		list.setAdapter(new Adapter(game_list));
		return list;
	}

	@Override
	public ResultState onLoad() {
		BaseProtocol<List<AppInfo>> protocol=new GameProtocol(); 
		game_list=protocol.getData(0);
		return check(game_list);
	}
	class Adapter extends MyBaseAdapter<AppInfo>{

		public Adapter(List<AppInfo> list) {
			super(list);
		}

		@Override
		public BaseHolder<AppInfo> getHolder() {
			return new AppHolder();
		}

		@Override
		public List<AppInfo> onLoad() {
			BaseProtocol<List<AppInfo>> protocol=new GameProtocol(); 
			List<AppInfo> game_more=new ArrayList<AppInfo>();
			game_more=protocol.getData(getListSize());
			return game_more;
		}
		
	}

}
