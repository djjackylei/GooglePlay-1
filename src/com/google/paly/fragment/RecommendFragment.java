package com.google.paly.fragment;

import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.paly.base.BaseFragment;
import com.google.paly.protocol.RecommendProtocol;
import com.google.paly.randomlayout.StellarMap;
import com.google.paly.ui.widgest.LoadingPage.ResultState;
import com.google.paly.utils.UIUtils;

public class RecommendFragment extends BaseFragment {
	
	private List<String> data;

	@Override
	public View onCreateSuccessView() {
		//飞入飞出的自定义控件
		StellarMap stellarMap=new StellarMap(UIUtils.getContext());
		int padding=UIUtils.dip2px(10);
		
		//设置飞入飞出文字跟边缘的内边距
		stellarMap.setInnerPadding(padding, padding, padding, padding);
		//设置数据设配器
		stellarMap.setAdapter(new Adapter());
		//维护内部总文字的条数 设置隐藏组和显示组的x和y的规则 
		stellarMap.setRegularity(9, 6);
		//指定哪组动画先执行
		stellarMap.setGroup(0, true);
		
		return stellarMap;
	}

	@Override
	public ResultState onLoad() {
		RecommendProtocol protocol = new RecommendProtocol();
		data = protocol.getData(0);
		return check(data);
	}
	class Adapter implements StellarMap.Adapter{
		//当前组的个数
		@Override
		public int getGroupCount() {
			return 2;
		}

		@Override
		public int getCount(int group) {
			return 15;
		}
		
		@Override
		public View getView(int group, int position, View convertView) {
			final TextView tv=new TextView(UIUtils.getContext());
			tv.setText(data.get(position));
			
			//设置文字背景颜色 (随机颜色 ) 
			int red=30+new Random().nextInt(210);
			int green=30+new Random().nextInt(210);
			int blue=30+new Random().nextInt(210);
			int rgb=Color.rgb(red, green, blue);
			//文字颜色随机
			tv.setTextColor(rgb);
			//文字字体大小随机
			int textSize=16+new Random().nextInt(10);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
			
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), tv.getText(), 0).show();
				}
			});
			
			return tv;
		}
		
		
		//第0组和第1组的效果切换执行
		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return 0;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			return 1;
		}
		
		
		
	}
}
