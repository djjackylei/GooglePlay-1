package com.google.paly.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 抽取线程管理类
 * ThreadPoolExecutor 管理线程
 * @author yanbinadmin
 * 
 */
public class ThreadManager {
	private static ThreadPoolProxy threadPoolProxy;

	public static ThreadPoolProxy getThreadPoolProxy() {
		if (threadPoolProxy == null) {
			synchronized (ThreadManager.class) {
				if (threadPoolProxy == null) {
					//静态使用静态
					threadPoolProxy = new ThreadPoolProxy(5, 5, 5L);
				}
			}
		}
		return threadPoolProxy;
	}

	public static class ThreadPoolProxy {
		private ThreadPoolExecutor poolExecutor;
		private int corePoolSize;
		private long keepAliveTime;
		private int maximumPoolSize;

		public ThreadPoolProxy(int corePoolSize, int maximumPoolSize,
				long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		public void execute(Runnable runnable) {
			if (poolExecutor == null) {
				poolExecutor = new ThreadPoolExecutor(
				// 主线程数量
						corePoolSize,
						// 最大线程数
						maximumPoolSize,
						// 线程存活时间
						keepAliveTime,
						// 线程存活单位
						TimeUnit.MILLISECONDS,
						// 工作队列
						new LinkedBlockingQueue<Runnable>(),
						// 默认线程工厂
						Executors.defaultThreadFactory(),
						// 线程异常处理对象
						// TODO　注意Ctrl＋1不能导入包　还有第二种方式导入包
						new AbortPolicy());
			}
			// 执行 don't 忘了
			poolExecutor.execute(runnable);
		}

		public void cancel(Runnable runnable){
			//容错处理
			if(runnable!=null && poolExecutor!=null && !poolExecutor.isShutdown()){
				//从线程池中将任务移除出去
				poolExecutor.getQueue().remove(runnable);
			}
		}
	}
}
