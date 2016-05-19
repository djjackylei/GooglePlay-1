package com.google.paly.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.google.paly.base.BaseFragment;
import com.google.paly.base.BaseHolder;
import com.google.paly.base.MyBaseAdapter;
import com.google.paly.bean.SubjectInfo;
import com.google.paly.holder.SubjectHolder;
import com.google.paly.protocol.SubjectProtocol;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.ui.widgest.MyListView;
import com.google.paly.utils.UIUtils;
/**
 * 专题界面处理 图片的三种处理方式
 * 	1设置图片属性为的scaleType fitXy 前景和背景一样大 图片会变形 
 * 						centerCrop 会从图片中截取一块跟背景成比例的图片 不变形 但图片显示不全
 *	 				这两种方式都不太好 都会导致图片或多或少的失去原来的样子
 *		最好的方式 是按照从服务器获取的图片 将图片按比例缩放 比如说服务器传过来的图片是444×183 计算它的长宽比例是2：1
 *									那么在客户端自定义个imageview用于接收比例 自定义属性 
 * @author yanbinadmin
 *
 */
public class SubjectFragment extends BaseFragment {
	private List<SubjectInfo> sub_data=new ArrayList<SubjectInfo>();
	//最外层是ListView
	@Override
	public View onCreateSuccessView() {
		ListView subject_list=new MyListView(UIUtils.getContext());
		subject_list.setAdapter(new Adapter(sub_data));
		return subject_list;
	}
	
	@Override
	public ResultState onLoad() {
		SubjectProtocol subjectProtocol = new SubjectProtocol();
		sub_data = subjectProtocol.getData(0);
		return check(sub_data);
	}
	class Adapter extends MyBaseAdapter<SubjectInfo>{
		public Adapter(List<SubjectInfo> list) {
			super(list);
		}
		@Override
		public BaseHolder<SubjectInfo> getHolder() {
			//用于绑定控件显示数据
			return new SubjectHolder();
		}
		@Override
		public List<SubjectInfo> onLoad() {
			SubjectProtocol subjectProtocol = new SubjectProtocol();
			List<SubjectInfo> new_data = subjectProtocol.getData(getListSize());
			return new_data;
		}
		
	}

}
