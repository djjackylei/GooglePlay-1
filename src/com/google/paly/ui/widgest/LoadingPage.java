package com.google.paly.ui.widgest;


import com.google.paly.R;
import com.google.paly.manager.ThreadManager;
import com.google.paly.utils.UIUtils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public abstract class LoadingPage extends FrameLayout {
	// 四种状态 一种对应一个布局
	// 默认状态
	private static final int STATE_UNLOAD = 0;
	// 下面四种状态每种状态对应一种view 只有钱三种状态对应的view是确定的
	// 正在加载
	private static final int STATE_LOADING = 1;
	private View view_unload;
	// 请求失败
	private static final int STATE_ERROR = 2;
	private View view_error;
	// 请求成功但没数据
	private static final int STATE_EMPTY = 3;
	private View view_empty;

	// 请求成功有数据 view不确定由每个具体的界面知道 抽象 回调
	private static final int STATE_SUCCESS = 4;
	private View view_success;

	private int current_state = STATE_UNLOAD;
	// 线程池初始化
	//private ExecutorService threadPool;
	private FrameLayout.LayoutParams layoutParams;

	// 因为不会在布局文件中使用它 所以只覆盖此种构造函数
	public LoadingPage(Context context) {
		super(context);
		//threadPool = Executors.newFixedThreadPool(5);
		layoutParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		
		// //先将所有的能够确认展示效果的界面添加到当前的帧布局中
		initView();
	}

	private void initView() {
		if (view_unload==null) {
			// 将显示processbar的view添加进入
			view_unload = UIUtils.inflate(R.layout.layout_loading);
			addView(view_unload,layoutParams);
		}
		if (view_error == null) {
			view_error = UIUtils.inflate(R.layout.layout_error);
			addView(view_error,layoutParams);
		}
		if (view_empty == null) {
			view_empty = UIUtils.inflate(R.layout.layout_empty);
			addView(view_empty,layoutParams);
		}
		// 根据状态在主线程中展示界面的操作 更新view只能在主线程
		showSafePage();
	}

	private void showSafePage() {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				// 保证展示数据是在主线程中
				// 根据状态显示不同的view
				showPage();
			}
		});
	}
	/**
	 * 根据状态展示不同的view
	 */
	private void showPage() {
		if (view_unload != null) {
			view_unload.setVisibility(current_state == STATE_UNLOAD
					|| current_state == STATE_LOADING ? View.VISIBLE
					: View.GONE);
		}
		if (view_error != null) {
			view_error
					.setVisibility(current_state == STATE_ERROR ? View.VISIBLE
							: View.GONE);
		}
		if (view_empty != null) {
			view_empty
					.setVisibility(current_state == STATE_EMPTY ? View.VISIBLE
							: View.GONE);
		}
		// 请求成功且有数据时该类不知道数据怎么展示 抽象回调
		if (view_success == null&&current_state == STATE_SUCCESS ) {
			view_success = onCreateSuccessView();
			if (view_success != null) {// 容错处理 防止子类返回null
				//注意不要忘了addview
				addView(view_success,layoutParams);
				view_success.setVisibility(View.VISIBLE);
			}
		}
		/*if(view_success!=null){
		}*/
	}

	// 状态的改变时根据什么而改变的？ 当然是请求网络的结果 所以 提供一个请求网络的方法
	// 请求网络只有三种状态 请求成功有数据 请求失败 请求成功无数据
	public void show() {
		// 状态初始化 状态归位
		if (current_state == STATE_ERROR || current_state == STATE_EMPTY
				|| current_state == STATE_SUCCESS) {
			current_state = STATE_UNLOAD;
		}
		if (current_state == STATE_UNLOAD) {
			// 请求网络 只能在子线程中进行 注意使用线程池进行管理
			ThreadManager.getThreadPoolProxy().execute(new Runnable() {
				@Override
				public void run() {
					// 当前类不知道如何去请求数据吧 所以当然是抽象+回调
					final ResultState resultState = onLoad();
					// 根据网络请求结果更新ui 为了保证在主线程更新UI
					UIUtils.runInMainThread(new Runnable() {
						@Override
						public void run() {
							if (resultState != null) {// 容错处理
								current_state = resultState.getResult_state();
								showPage();
							}
						}
					});
				}
			});
		}
	}

	/** 根据请求网络的结果返回请求结果状态 */
	public abstract ResultState onLoad();

	/** 返回请求成功界面应该如何展示的view */
	public abstract View onCreateSuccessView();

	public enum ResultState {
		REQUEST_ERROR(2), REQUEST_EMPTY(3), REQUEST_SUCCESS(4);
		private int result_state;

		ResultState(int result_state) {
			this.result_state = result_state;
		}
		public int getResult_state() {
			return result_state;
		}
	}
}
