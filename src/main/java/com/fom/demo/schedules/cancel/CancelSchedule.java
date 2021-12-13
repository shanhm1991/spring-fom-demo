package com.fom.demo.schedules.cancel;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.proxy.TerminateHandler;
import org.springframework.fom.proxy.TaskCancelHandler;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * <p>如果想取消不可中断的任务，比如等待套接字返回，或者无限循环，那么此时线程已经无法响应中断，
 * 只能通过关闭套接字连接，或者增加循环结束标志等手段来实现。
 * 
 * <p>这时，可以通过实现接口<b>TaskCancelHandler</b>来自定义任务取消策略，对于取消策略有两个调用时机：
 * 检测到任务超时，或者被外部shutdown关闭
 * 
 * <p>如果想自定义shutdown之后的处理，比如释放一些资源，那么可以实现接口<b>ScheduleTerminator</b>。
 * 另外，通过<b>execOnLoad</b>可以设置任务在启动时立即执行。
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(cron = "0 0/11 * * * ?", execOnLoad = true, taskOverTime = 300000, remark = "自定义任务取消策略")
public class CancelSchedule implements TaskCancelHandler, TerminateHandler {
	
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
	public void handleCancel(String taskId, long costTime) {
		off = true;
		LOG.info("handleCancel：任务被取消{}，已经耗时{}ms", taskId, costTime); 
		
	}

	@Override
	public void onTerminate(long execTimes, long lastExecTime) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastExecTime);
		LOG.info("onTerminate：任务关闭，共执行{}次任务，最后一次执行时间为{}", execTimes, date);
		
	}
}
