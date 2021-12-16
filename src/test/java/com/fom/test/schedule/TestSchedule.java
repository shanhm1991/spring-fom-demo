package com.fom.test.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.fom.Result;
import org.springframework.fom.annotation.FomSchedule;
import org.springframework.fom.proxy.CompleteHandler;
import org.springframework.fom.proxy.ScheduleFactory;
import org.springframework.fom.proxy.TaskCancelHandler;

/**
 * 
 * @author shanhm1991@163.com
 *
 */
@FomSchedule(execOnLoad=true, fixedDelay=15000, threadCore=4, taskOverTime=5000)
public class TestSchedule implements ScheduleFactory<Long>, CompleteHandler<Long>, TaskCancelHandler { 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestSchedule.class);

	@Override
	public List<TestTask> newSchedulTasks() throws Exception {
		List<TestTask> tasks = new ArrayList<>();
		for(int i = 0;i < 10; i++){
			tasks.add(new TestTask());
		} 
		return tasks;
	} 
	
	@Override
	public void onComplete(long times, long lastTime, List<Result<Long>> results) throws Exception {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastTime);
		LOGGER.info("onComplete：第{}次在{}提交的任务全部完成", times, date);
	}

	@Override
	public void handleCancel(String taskId, long costTime) throws Exception {
		LOGGER.info("handleCancel：任务被取消{}，已经耗时{}ms", taskId, costTime); 
	}
}
