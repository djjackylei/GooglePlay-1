package com.google.paly.fragment;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.paly.base.BaseFragment;
import com.google.paly.base.BaseHolder;
import com.google.paly.base.MyBaseAdapter;
import com.google.paly.bean.CategoryBean;
import com.google.paly.holder.CategoryItemHolder;
import com.google.paly.holder.TitileHolder;
import com.google.paly.protocol.CategoryProtocol;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.ui.widgest.MyListView;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;

/**
 * ListView加载不同种类的条目
 * 
 * @author yanbinadmin
 * 
 */
public class CategoryFragment extends BaseFragment {

	private List<CategoryBean> list;

	@Override
	public View onCreateSuccessView() {
		ListView gridView = new MyListView(UIUtils.getContext());
		//gridView.setNumColumns(3);
		int padding=UIUtils.dip2px(5);
		gridView.setPadding(padding, padding, padding, padding);
		gridView.setOnItemClickListener(null);
		
		gridView.setAdapter(new Adapter(list));
		return gridView;
	}

	@Override
	public ResultState onLoad() {
		CategoryProtocol categoryProtocol = new CategoryProtocol();
		list = categoryProtocol.getData(0);
		return check(list);
	}

	// 数据设配器
	class Adapter extends MyBaseAdapter<CategoryBean> {
		private int currentPosition;
		private BaseHolder baseHolder;
		public Adapter(List<CategoryBean> list) {
			super(list);
		}

		@Override
		public BaseHolder<CategoryBean> getHolder() {
			if(getList().get(currentPosition).isTitle){
				//标题条目
				baseHolder=new TitileHolder(getList().get(currentPosition));
			}else{
				//一般条目
				baseHolder=new CategoryItemHolder();
			}
			return baseHolder;
		}
		
		// 维护三种条目
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;
		}

		// 种类多了一个类型 并且不需要加载更多
		@Override
		public int getInnerItemType(int position) {
			// 怎么判断是一般条目 还是标题条目 通过list和索引值 获取当前bean中的istitel
			//这边怎么会报角标越界的 ？ 最后一个条目loadmore 让他越界了
			if (getList().get(position).isTitle) {
				return 2;
			} else {
				return super.getInnerItemType(position);
			}
		}
		
		// 获取当前position 其他的仍使用父类的逻辑
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			currentPosition = position;
			return super.getView(position, convertView, parent);
		}

		
		//没有加载更多
		@Override
		public Boolean isHasMore() {
			return false;
		}

		@Override
		public List<CategoryBean> onLoad() {
			return null;
		}

	}
}
