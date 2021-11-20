package com.fom.demo.schedules.cancel;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.interceptor.ScheduleTerminator;
import org.springframework.fom.interceptor.TaskTimeoutHandler;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * <p>有时任务不一定支持中断取消，比如等待套接字返回，或者有条件的无限循环
 * 
 * <p>那么可以通过实现接口 TaskTimeoutHandler 自定义任务的超时取消策略
 * 
 * <p>另外，实现接口 ScheduleTerminator 也可以自定义定时任务关闭时的处理，比如释放一些资源
 * 
 * <p>通过 execOnLoad 可以设置任务是否在启动时立即执行，默认是启动后按照定时计划决定首次执行时间
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(cron = "0 0/11 * * * ?", execOnLoad = true, taskOverTime = 300000, remark = "自定义取消超时任务")
public class CancelSchedule implements TaskTimeoutHandler, ScheduleTerminator {
	
	private static final Logger LOG = LoggerFactory.getLogger(CancelSchedule.class);
	
	private volatile boolean off = false;

	@Scheduled
	public void exec() { 
		LOG.info("task executing ...");
		while(!off){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				//ignore
			}
		}
	}

	@Override
	public void handleTimeout(String taskId, long costTime) {
		off = true;
		LOG.info("任务被取消{}，已经耗时{}ms", taskId, costTime); 
		
	}

	@Override
	public void onScheduleTerminate(long execTimes, long lastExecTime) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastExecTime);
		LOG.info("任务关闭，共执行{}次任务，最后一次执行时间为{}", execTimes, date);
		
	}
}
