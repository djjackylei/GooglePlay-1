package com.google.paly.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application {
	private static Context context;
	private static Thread mThread;
	private static int mThreadId;
	private static Handler handler;
	
	@Override
	public void onCreate() {
		//程序1启动就会调用
		context=getApplicationContext();
		mThread=Thread.currentThread();
		mThreadId=android.os.Process.myTid();
		handler=new Handler();
		super.onCreate();
	}

	public static Context getContext() {
		return context;
	}

	public static Thread getmThread() {
		return mThread;
	}

	public static int getmThreadId() {
		return mThreadId;
	}

	public static Handler getHandler() {
		return handler;
	}
	
}
