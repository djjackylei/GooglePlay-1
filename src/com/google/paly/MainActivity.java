package com.google.paly;

import com.google.paly.base.BaseActivity;
import com.google.paly.factory.FragmentFactory;
import com.google.paly.ui.widgest.PagerTab;
import com.google.paly.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	@ViewInject(R.id.pagertab)
	private PagerTab pagertab;
	@ViewInject(R.id.viewpager)
	private ViewPager viewpager;
	private String[] titles;
	@ViewInject(R.id.drawer_layout)
	private DrawerLayout drawer_layout;
	private ActionBarDrawerToggle toggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		initTitle();
		//初始化actionbar
		initActionBar();
		
		viewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		// 让指针和viewpager有一个意义对应的关系
		pagertab.setViewPager(viewpager);
		pagertab.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				FragmentFactory.createOrGetFragment(arg0).show();
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	

	private void initTitle() {
		titles = UIUtils.getStringArray(R.array.tab_names);
	}

	class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		// 返回根据索引找到的fragment对象 ，viewpager中要获取的是fragment中oncreateView中返回的view对象
		// 在智慧北京项目中，让数据设配器继承至pagerAdapter手动的实现添加view的方法
		@Override
		public Fragment getItem(int arg0) {
			// Fragment有很多个 抽取基类 使用工厂类进行管理 先从内存中查找是否已经有了fragment对象 有就拿过来用没有就创建
			// 享元设计模式
			return FragmentFactory.createOrGetFragment(arg0);
		}
		@Override
		public int getCount() {
			return titles.length;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}
	
	
	
	
	//---------------处理菜单的逻辑--------------------
	private void initActionBar() {
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setTitle("");
		//设置左上角可以被Note: This element neither has attached source nor attached Javadoc and hence no Javadoc could be found.
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		//设置菜单可用可被点击
		supportActionBar.setHomeButtonEnabled(true);
		
		//设置左侧侧拉可弹出可侧拉
		
		toggle = new ActionBarDrawerToggle(this, drawer_layout,
				//指定菜单图片
				R.drawable.ic_drawer_am, 
				//无意义的描述信息
				R.string.drawer_close, R.string.drawer_close);
		
		//TODO 重要调用同步方法使按钮和侧拉菜单绑定点击
		toggle.syncState();
		//最后在下面的方法zzhong 
		
	}
	/**
	 * TODO  actionBar 见布局文件
	 * 处理actionbar菜单的点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item)||toggle.onOptionsItemSelected(item);
	}
	//当搜索文本发生变化时 
	
}