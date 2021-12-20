package com.fom.demo.schedules.timeout;

import java.util.Random;

import org.springframework.fom.Task;
import org.springframework.fom.proxy.TaskCancelHandler;

/**
 * 
 * @author shanhm1991@163.com
 *
 */
public class TimeoutTask extends Task<Long> implements TaskCancelHandler {

	@Override
	public Long exec() throws InterruptedException { 
		long sleep = new Random().nextInt(10000);
		logger.info("task executing ...");
		Thread.sleep(sleep);
		return sleep;
	}

	@Override
	public void handleCancel(String taskId, long costTime) throws Exception {
		logger.info("task handleCancel：取消任务[{}]，耗时={}ms", taskId, costTime);
	}
}
