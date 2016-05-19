package com.google.paly.factory;

import java.util.HashMap;
import java.util.Map;

import com.google.paly.base.BaseFragment;
import com.google.paly.fragment.AppFragment;
import com.google.paly.fragment.CategoryFragment;
import com.google.paly.fragment.GameFragment;
import com.google.paly.fragment.HomeFragment;
import com.google.paly.fragment.HotFragment;
import com.google.paly.fragment.RecommendFragment;
import com.google.paly.fragment.SubjectFragment;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	private static Map<Integer, BaseFragment> map = new HashMap<Integer, BaseFragment>();

	public static BaseFragment createOrGetFragment(int arg0) {
		BaseFragment baseFragment = null;
		if (map.containsKey(arg0)) {
			baseFragment = map.get(arg0);
		} else {// 创建
			switch (arg0) {
			case 0:// 首页
				baseFragment=new HomeFragment();
				break;
			case 1:// 应用
				baseFragment=new AppFragment();
				break;
			case 2:// 游戏
				baseFragment=new GameFragment();
				break;
			case 3:// 专题
				baseFragment=new SubjectFragment();
				
				break;
			case 4:// 推荐
				baseFragment=new RecommendFragment();

				break;
			case 5:// 分类
				baseFragment=new CategoryFragment();
				break;
			case 6:// 排行
				baseFragment=new HotFragment();
				break;
			}
			//将新创建的fragment对象存入内存中 让工厂类进行管理
			map.put(arg0, baseFragment);
		}
		return baseFragment;
	}

}
